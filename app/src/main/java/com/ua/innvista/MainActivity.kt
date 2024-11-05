package com.ua.innvista

import android.os.Bundle
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.ua.innvista.data.AppDatabase
import com.ua.innvista.navigation.BottomBar
import com.ua.innvista.navigation.NavHostComposable
import com.ua.innvista.navigation.NavigationDrawerSheet
import com.ua.innvista.navigation.Screens
import com.ua.innvista.navigation.TopBar
import com.ua.innvista.profile.ProfileViewModel
import com.ua.innvista.ui.theme.InnVistaTheme
import com.ua.innvista.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            var isDarkModeEnabled by remember { mutableStateOf(false) }

            val viewModel = hiltViewModel<ProfileViewModel>()
            val name by viewModel.name.collectAsState()

            val navController = rememberNavController()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

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
        }
    }
}

@Composable
fun MyApp() {
    var isDarkModeEnabled by remember { mutableStateOf(false) }

}
