package com.ua.innvista.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ua.innvista.R
import com.ua.innvista.notifications.Notifications
import com.ua.innvista.profile.Profile
import com.ua.innvista.hotel.HotelSearch
import com.ua.innvista.ui.Dimensions
import com.ua.innvista.wishlist.Wishlist

@Composable
fun NavHostComposable(innerPadding: PaddingValues, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Search.name,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(Dimensions.padding)
    ) {
        composable(route = Screens.Notifications.name) {
            Notifications()
        }
        composable(route = Screens.Search.name) {
            HotelSearch()
        }
        composable(route = Screens.Wishlist.name) {
            Wishlist()
        }
        composable(route = Screens.Profile.name) {
            Profile()
        }
    }
}
