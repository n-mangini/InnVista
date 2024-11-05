package com.ua.innvista.navigation

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ua.innvista.R
import com.ua.innvista.ui.theme.padding
import com.ua.innvista.ui.theme.paddingBig
import com.ua.innvista.ui.theme.spacerBig

@Composable
fun NavigationDrawerSheet(
    onClose: () -> Unit,
    onLogout: (Context) -> Unit,
    userName: String
) {
    val context = LocalContext.current

    ModalDrawerSheet {
        Column(modifier = Modifier.padding(paddingBig)) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.close),
                modifier = Modifier
                    .clickable { onClose() }
                    .align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(spacerBig))

            Text(
                //TODO check if logged in to use welcome msg
                text = "${stringResource(R.string.welcome_drawer)}, $userName",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = padding)
            )

            Spacer(modifier = Modifier.height(spacerBig))

            Button(
                onClick = { onLogout(context)},
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(text = stringResource(R.string.logout))
            }
        }
    }
}

@Composable
@Preview
fun NavigationDrawerSheetPreview() {
    NavigationDrawerSheet(
        onClose = {},
        onLogout = {},
        userName = "John Doe"
    )
}
