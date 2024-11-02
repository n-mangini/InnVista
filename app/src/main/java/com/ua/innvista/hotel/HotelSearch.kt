package com.ua.innvista.hotel

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ua.innvista.R
import com.ua.innvista.common.HotelItem
import com.ua.innvista.common.SearchBar
import com.ua.innvista.ui.theme.actionIconSize
import com.ua.innvista.ui.theme.progressSize
import com.ua.innvista.utils.showToast
import com.ua.innvista.wishlist.WishlistViewModel

@Composable
fun HotelSearch() {
    val hotelSearchViewModel = hiltViewModel<HotelSearchViewModel>()
    val wishlistViewModel = hiltViewModel<WishlistViewModel>()

    val hotels by hotelSearchViewModel.hotels.collectAsState()
    val loading by hotelSearchViewModel.loadHotels.collectAsState()
    val showRetry by hotelSearchViewModel.showRetry.collectAsState()

    var selectedHotel by remember { mutableStateOf<HotelModel?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    Column {
        SearchBar(query = searchQuery, onQueryChanged = { searchQuery = it })

        when {
            loading -> LoadingView()
            showRetry -> RetryView(onRetry = { hotelSearchViewModel.retryLoadingHotels() })

            selectedHotel != null -> HotelDetailModal(
                hotel = selectedHotel!!,
                onDismissRequest = { selectedHotel = null }
            )

            else -> HotelList(
                hotels = hotels.filter { hotel ->
                    hotel.title.contains(searchQuery, ignoreCase = true) || hotel.location.contains(
                        searchQuery,
                        ignoreCase = true
                    )
                },
                onHotelSelected = { selectedHotel = it },
                wishlistViewModel = wishlistViewModel
            )
        }
    }
}

@Composable
fun HotelList(
    hotels: List<HotelModel>,
    onHotelSelected: (HotelModel) -> Unit,
    wishlistViewModel: WishlistViewModel
) {
    val context = LocalContext.current

    // Collect the wishlist from the ViewModel as a state
    val wishlist by wishlistViewModel.wishlist.collectAsState(initial = emptyList())

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(hotels) { hotel ->

            val isInWishlist = remember { mutableStateOf(false) }

            // Launch a coroutine to check if the hotel is in the wishlist
            LaunchedEffect(wishlist) {
                isInWishlist.value = wishlist.any { it.id == hotel.id }
            }

            HotelItem(
                hotel = hotel,
                onItemClick = { onHotelSelected(hotel) },
                iconButtonComposable = {
                    WishlistIconButton(
                        isInWishlist = isInWishlist.value,
                        onIconClick = {
                            handleAddToWishlist(
                                context,
                                wishlistViewModel,
                                hotel
                            )
                        })
                }
            )
        }
    }
}

@Composable
fun WishlistIconButton(isInWishlist: Boolean, onIconClick: () -> Unit) {
    IconButton(onClick = onIconClick) {
        Icon(
            imageVector = if (isInWishlist) Icons.Default.Star else Icons.Outlined.Star,
            contentDescription = stringResource(
                if (isInWishlist) R.string.remove_from_wishlist else R.string.add_to_wishlist
            ),
            tint = if (isInWishlist) colorResource(id = R.color.star) else colorResource(id = R.color.gray),
            modifier = Modifier.size(actionIconSize)
        )
    }
}

@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(progressSize)
                .align(Alignment.Center),
            color = colorResource(id = R.color.appBlueLight),
            trackColor = colorResource(id = R.color.appBlue),
        )
    }
}

@Composable
fun RetryView(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.retry),
            fontWeight = FontWeight.Bold,
        )
        Text(text = stringResource(R.string.loadhotels))
        Button(onClick = { onRetry() }) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

fun handleAddToWishlist(
    context: Context, viewModel: WishlistViewModel,
    hotel: HotelModel
) {
    viewModel.addHotel(hotel)
    { wasAdded ->
        if (wasAdded) {
            showToast(
                context,
                context.getString(R.string.added_to_wishlist)
            )
        } else {
            showToast(
                context,
                context.getString(R.string.already_in_wishlist)
            )
        }
    }
}

@Composable
@Preview
fun PreviewHotelItem() {
    HotelItem(
        hotel = HotelModel(
            1L,
            title = "Hotel 1",
            imgUrl = "",
            location = "Location 1",
            description = "Description 1",
            price = "$912"
        ),
        onItemClick = {},
        iconButtonComposable = { WishlistIconButton(true) { } }
    )
}
