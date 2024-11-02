package com.ua.innvista.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.ua.innvista.R
import com.ua.innvista.hotel.WishlistIconButton
import com.ua.innvista.hotel.HotelModel
import com.ua.innvista.ui.theme.cornerRadius
import com.ua.innvista.ui.theme.iconSize
import com.ua.innvista.ui.theme.imgHeight
import com.ua.innvista.ui.theme.imgWidth
import com.ua.innvista.ui.theme.padding
import com.ua.innvista.ui.theme.spacer
import com.ua.innvista.ui.theme.spacerBig

/**
 * Reusable HotelItem composable
 * This composable is used across home and wishlist screens, where the only difference is
 * the icon and the action performed (e.g., save to wishlist vs remove from wishlist).
 *
 * @param hotel The hotel model to display.
 * @param onItemClick Action to perform when the hotel item is clicked
 * @param iconButtonComposable Composable representing the action button (e.g., save/remove) with its own icon and logic.
 */
@Composable
fun HotelItem(
    hotel: HotelModel,
    onItemClick: (HotelModel) -> Unit,
    iconButtonComposable: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
            .clickable { onItemClick(hotel) },
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(padding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(imgWidth)
                    .height(imgHeight)
                    .clip(RoundedCornerShape(cornerRadius))
            ) {
                AsyncImage(
                    model = hotel.imgUrl,
                    contentDescription = hotel.title,
                    placeholder = painterResource(id = R.drawable.innvista),
                    error = painterResource(id = R.drawable.innvista),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(spacerBig))

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = hotel.title,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )
                    iconButtonComposable()
                }

                Row {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = stringResource(R.string.location),
                        modifier = Modifier
                            .size(iconSize)
                            .align(Alignment.Top)
                    )
                    Spacer(modifier = Modifier.size(spacer))
                    Text(
                        text = hotel.location,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Top)
                    )
                }
                Spacer(modifier = Modifier.size(spacer))
                Text(text = hotel.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.size(spacer))
                Text(
                    text = hotel.price,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.End)
                )
            }
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
