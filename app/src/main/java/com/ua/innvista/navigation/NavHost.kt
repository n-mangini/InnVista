package com.ua.innvista.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ua.innvista.hotel.HotelSearch
import com.ua.innvista.profile.Profile
import com.ua.innvista.ui.theme.padding
import com.ua.innvista.wishlist.Wishlist

@Composable
fun NavHostComposable(
    innerPadding: PaddingValues,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Search.name,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(padding)
    ) {
        composable(route = Screens.Search.name) {
            HotelSearch()
        }
        composable(route = Screens.Wishlist.name) {
            Wishlist(onSearchClick = {
                navController.navigate(Screens.Search.name)
            })
        }
        composable(route = Screens.Profile.name) {
            Profile()
        }
    }
}
