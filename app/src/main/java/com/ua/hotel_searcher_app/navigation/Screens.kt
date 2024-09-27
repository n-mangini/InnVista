package com.ua.hotel_searcher_app.navigation

enum class Screens {
    Profile,
    Search,
    Wishlist,

    Notifications,

    HotelDetail,
}

val basePages = listOf(
    Screens.Profile.name,
    Screens.Search.name,
    Screens.Wishlist.name,
)
