package com.ua.innvista

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.ua.innvista.data.AppDatabase
import com.ua.innvista.navigation.BottomBar
import com.ua.innvista.navigation.NavHostComposable
import com.ua.innvista.navigation.NavigationDrawerSheet
import com.ua.innvista.navigation.Screens
import com.ua.innvista.navigation.TopBar
import com.ua.innvista.profile.ProfileViewModel
import com.ua.innvista.security.BiometricAuthManager
import com.ua.innvista.ui.theme.InnVistaTheme
import com.ua.innvista.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @Inject
    lateinit var biometricAuthManager: BiometricAuthManager
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            var isAuthenticated by remember { mutableStateOf(false) }

            if (isAuthenticated) {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                var isDarkModeEnabled by remember { mutableStateOf(false) }
                val viewModel = hiltViewModel<ProfileViewModel>()
                val name by viewModel.name.collectAsState()

                InnVistaTheme(darkTheme = isDarkModeEnabled) {
                    ModalNavigationDrawer(drawerContent = {
                        NavigationDrawerSheet(
                            onClose = {
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            //TODO check user logged in to show conditional toast
                            onLogout = { context ->
                                scope.launch {
                                    viewModel.logout(context)
                                    drawerState.close()
                                }
                                showToast(context, getString(R.string.logged_out))
                                navController.navigate(Screens.Profile.name)
                            },
                            userName = name
                        )
                    }, drawerState = drawerState) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Scaffold(
                                topBar = {
                                    TopBar(
                                        navController = navController,
                                        openDrawer = {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        }
                                    )
                                },
                                bottomBar = {
                                    BottomBar { navController.navigate(it) }
                                },
                            ) { innerPadding ->
                                NavHostComposable(
                                    innerPadding,
                                    navController,
                                    isDarkModeEnabled = isDarkModeEnabled,
                                    onToggleDarkMode = { isDarkModeEnabled = it }
                                )
                            }
                        }
                    }
                }
            } else {
                BiometricAuthentication(
                    isAuthenticated,
                    onSuccess = { isAuthenticated = true },
                    biometricAuthManager
                )
            }
        }
    }
}

@Composable
fun BiometricAuthentication(
    isAuthenticated: Boolean,
    onSuccess: () -> Unit,
    biometricAuthManager: BiometricAuthManager
) {
    val context = LocalContext.current
    val biometricManager = remember { BiometricManager.from(context) }
    val isBiometricAvailable = remember {
        biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
    }

    when (isBiometricAvailable) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            // Biometrics or device credentials (PIN/password) are available
            if (!isAuthenticated) {
                biometricAuthManager.authenticate(
                    context,
                    onError = {
                        //TODO extract string
                        Toast.makeText(context, "Authentication error", Toast.LENGTH_SHORT).show()
                    },
                    onSuccess = onSuccess,
                    onFail = {
                        //TODO extract string
                      Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            Text(text = "This phone does not support biometric authentication.")
        }
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            Text(text = "Biometric hardware is currently unavailable.")
        }
        BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
            Text(text = "Security update required for biometric authentication.")
        }
        BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
            Text(text = "Biometric authentication is not supported on this Android version.")
        }
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            Text(text = "No biometric credentials enrolled. Use device credentials to authenticate.")
            // You might want to prompt the user to set up biometrics if available
        }
        else -> {
            Text(text = "Biometric authentication is not available.")
        }
    }
}


@Composable
fun MyApp() {
    var isDarkModeEnabled by remember { mutableStateOf(false) }
}
