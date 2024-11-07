package com.ua.innvista.navigation

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.ua.innvista.R
import com.ua.innvista.ui.theme.cornerRadius
import com.ua.innvista.ui.theme.padding
import com.ua.innvista.ui.theme.paddingBig
import com.ua.innvista.ui.theme.spacerBig

@Composable
fun NavigationDrawerSheet(
    onClose: () -> Unit,
    onLogoutConfirmed: (Context) -> Unit,
    userName: String
) {
    val context = LocalContext.current
    var showLogoutWarning by remember { mutableStateOf(false) }

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
                text = if (userName.isEmpty()) {
                    stringResource(R.string.welcome_drawer)
                } else {
                    "${stringResource(R.string.welcome_drawer)}, $userName"
                },
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = padding)
            )

            Spacer(modifier = Modifier.height(spacerBig))

            Button(
                onClick = { showLogoutWarning = true },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(text = stringResource(R.string.logout))
            }
            if (showLogoutWarning) {
                LogoutWarningModal(
                    onConfirmLogout = {
                        showLogoutWarning = false
                        onLogoutConfirmed(context)
                    },
                    onDismissRequest = { showLogoutWarning = false }
                )
            }
        }
    }
}

@Composable
fun LogoutWarningModal(
    onDismissRequest: () -> Unit,
    onConfirmLogout: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(cornerRadius),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(padding)
        ) {
            Column(
                modifier = Modifier.padding(paddingBig),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.logout_warning_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = paddingBig)
                )

                Text(
                    text = stringResource(R.string.logout_warning_message),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = paddingBig)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(paddingBig),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    // Cancel
                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }

                    // Confirm Logout
                    Button(
                        onClick = {
                            onDismissRequest()
                            onConfirmLogout()
                        }
                    ) {
                        Text(text = stringResource(R.string.confirm_logout))
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun NavigationDrawerSheetPreview() {
    NavigationDrawerSheet(
        onClose = {},
        onLogoutConfirmed = {},
        userName = "John Doe"
    )
}
