package com.ua.innvista.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.ua.innvista.R
import com.ua.innvista.ui.Dimensions

@Composable
fun NavigationDrawerSheet(
    onClose: () -> Unit,
) {
    ModalDrawerSheet {
        Column(modifier = Modifier.padding(Dimensions.paddingBig)) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",
                modifier = Modifier.clickable { onClose() }
            )
        }
    }
}

@Composable
@Preview
fun NavigationDrawerSheetPreview() {
    NavigationDrawerSheet {}
}