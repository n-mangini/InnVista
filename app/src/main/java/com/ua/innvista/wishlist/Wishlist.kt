package com.ua.innvista.wishlist

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ua.innvista.R
import com.ua.innvista.common.HotelItem
import com.ua.innvista.data.toModel
import com.ua.innvista.hotel.HotelDetailModal
import com.ua.innvista.hotel.HotelModel
import com.ua.innvista.ui.theme.cornerRadius
import com.ua.innvista.ui.theme.imgHeight
import com.ua.innvista.ui.theme.padding
import com.ua.innvista.ui.theme.paddingBig
import com.ua.innvista.ui.theme.spacerBig
import com.ua.innvista.utils.showToast

@Composable
fun Wishlist(onSearchClick: () -> Unit) {
    val viewModel = hiltViewModel<WishlistViewModel>()

    val wishlist by viewModel.wishlist.collectAsState(initial = emptyList())
    var selectedHotel by remember { mutableStateOf<HotelModel?>(null) }

    when {
        wishlist.isEmpty() -> WishlistEmpty(onSearchClick)
        selectedHotel != null -> HotelDetailModal(
            hotel = selectedHotel!!,
            onDismissRequest = { selectedHotel = null })

        else -> WishlistContent(
            wishlist = wishlist.map { it.toModel() },
            onHotelSelected = { selectedHotel = it },
            viewModel = viewModel
        )
    }
}

@Composable
fun WishlistContent(
    wishlist: List<HotelModel>,
    onHotelSelected: (HotelModel) -> Unit,
    viewModel: WishlistViewModel
) {
    val context = LocalContext.current

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(wishlist) { hotel ->
            HotelItem(
                hotel = hotel,
                onItemClick = { onHotelSelected(hotel) },
                iconButtonComposable = {
                    RemoveFromWishlistIcon(
                        onIconClick = { handleRemoveFromWishlist(context, viewModel, hotel) }
                    )
                }
            )
        }
    }
}

@Composable
fun RemoveFromWishlistIcon(onIconClick: () -> Unit) {
    IconButton(onClick = { onIconClick() }) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(R.string.delete_from_wishlist)
        )
    }
}

@Composable
fun WishlistEmpty(
    onSearchClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingBig),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.innvista),
            contentDescription = stringResource(R.string.app_logo),
            modifier = Modifier
                .fillMaxWidth()
                .height(imgHeight)
                .clip(RoundedCornerShape(cornerRadius)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(spacerBig))

        Text(
            text = stringResource(R.string.your_wishlist_is_empty),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = padding)
        )

        Text(
            text = stringResource(R.string.browse_and_add_items_to_your_wishlist),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = paddingBig)
        )

        Spacer(modifier = Modifier.height(spacerBig))

        // Start Searching Button
        Button(
            onClick = onSearchClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.padding(horizontal = paddingBig)
        ) {
            Text(text = stringResource(R.string.start_searching))
        }
    }
}


fun handleRemoveFromWishlist(context: Context, viewModel: WishlistViewModel, hotel: HotelModel) {
    viewModel.deleteHotel(hotel.id)
    showToast(context, context.getString(R.string.removed_from_wishlist))
}

@Composable
@Preview
fun PreviewHotelItem() {
    HotelItem(
        hotel = HotelModel(
            id = 1L,
            title = "Hotel 1",
            imgUrl = "",
            location = "Location 1",
            description = "Description 1",
            price = "$912"
        ),
        onItemClick = {},
        iconButtonComposable = { RemoveFromWishlistIcon { } }
    )
}
