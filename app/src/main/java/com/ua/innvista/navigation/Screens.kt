package com.ua.innvista.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ua.innvista.R

enum class Screens {
    Profile,
    Search,
    Wishlist,
}

val basePages = listOf(
    Screens.Profile.name,
    Screens.Search.name,
    Screens.Wishlist.name,
)

@Composable
fun getScreenTitle(screen: Screens): String {
    val context = LocalContext.current
    return when (screen) {
        Screens.Profile -> context.getString(R.string.screen_profile)
        Screens.Search -> context.getString(R.string.screen_search)
        Screens.Wishlist -> context.getString(R.string.screen_wishlist)
    }
}
