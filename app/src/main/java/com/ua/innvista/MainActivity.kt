package com.ua.innvista

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import com.ua.innvista.ui.theme.padding
import com.ua.innvista.ui.theme.paddingBig
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

    override fun onResume() {
        super.onResume()

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
    var isBiometricAvailable = biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when(lifecycleState) {
            Lifecycle.State.RESUMED -> {
                isBiometricAvailable = biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            }
            else -> {}
        }
    }

    when (isBiometricAvailable) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            // Biometrics or device credentials (PIN/password) are available
            if (!isAuthenticated) {
                biometricAuthManager.authenticate(
                    context,
                    onError = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.authentication_error), Toast.LENGTH_SHORT
                        ).show()
                    },
                    onSuccess = onSuccess,
                    onFail = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.authentication_failed), Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }

        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            Text(text = stringResource(R.string.this_phone_does_not_support_biometric_authentication))
        }

        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            Text(text = stringResource(R.string.biometric_hardware_is_currently_unavailable))
        }

        BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
            Text(text = stringResource(R.string.security_update_required_for_biometric_authentication))
        }

        BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
            Text(text = stringResource(R.string.biometric_authentication_is_not_supported_on_this_android_version))
        }

        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            EnrollUserBiometrics(context)
        }

        else -> {
            Text(text = stringResource(R.string.biometric_authentication_is_not_available))
        }
    }
}

@Composable
fun EnrollUserBiometrics(context: Context) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingBig),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(paddingBig)
        ) {
            Text(
                text = stringResource(R.string.no_biometric_credentials_enrolled_use_device_credentials_to_authenticate),
                modifier = Modifier.padding(padding)
            )

            Button(onClick = {
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }
                context.startActivity(enrollIntent)
            }) {
                Text(stringResource(R.string.set_up_biometrics))
            }
        }
    }
}
