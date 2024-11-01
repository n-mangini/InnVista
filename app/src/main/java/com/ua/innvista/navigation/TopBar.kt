package com.ua.innvista.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ua.innvista.R
import com.ua.innvista.common.TabBarBadgeView
import com.ua.innvista.ui.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavHostController,
    openDrawer: () -> Unit,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val showBackButton by remember(currentBackStackEntry) {
        derivedStateOf {
            navController.previousBackStackEntry != null && !basePages.contains(navController.currentDestination?.route)
        }
    }

    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = if (!showBackButton) Icons.Filled.Menu else Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                modifier = Modifier
                    .padding(Dimensions.padding)
                    .clickable {
                        if (showBackButton) {
                            navController.popBackStack()
                        } else {
                            openDrawer()
                        }
                    }
            )
        },
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        actions = {
            Row {
                BadgedBox(badge = { TabBarBadgeView(912) }) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "",
                        modifier = Modifier.clickable { navController.navigate(Screens.Notifications.name) }
                    )
                }
            }
        },
        modifier = Modifier.padding(Dimensions.padding),
    )
}

@Composable
@Preview
fun TopBarPreview() {
    TopBar(navController = rememberNavController(), openDrawer = {})
}
