package com.ua.innvista.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ua.innvista.R
import com.ua.innvista.ui.theme.paddingBig
import com.ua.innvista.ui.theme.profileIconSize
import com.ua.innvista.ui.theme.spacerBig

@Composable
fun Profile(
    isDarkModeEnabled: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    val viewModel = hiltViewModel<ProfileViewModel>()
    val name by viewModel.name.collectAsState()
    val surname by viewModel.surname.collectAsState()

    if (name.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        )
        {
            SetUsername(
                onSave = { enteredName, enteredSurname ->
                    viewModel.saveUserToDataStore(
                        enteredName,
                        enteredSurname
                    )
                }
            )
        }
    } else {
        UserProfile(
            name = name,
            surname = surname,
            isDarkModeEnabled = isDarkModeEnabled,
            onToggleDarkMode = onToggleDarkMode
        )
    }
}

@Composable
fun SetUsername(
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.profile_prompt),
            fontWeight = FontWeight.Bold,
        )

        TextField(
            value = name,
            onValueChange = { name = it },
            label = {
                Text(text = stringResource(R.string.name))
            },
        )

        TextField(
            value = surname,
            onValueChange = { surname = it },
            label = {
                Text(text = stringResource(R.string.surname))
            },
        )

        Button(
            onClick = { onSave(name, surname) },
        ) {
            Text(text = stringResource(R.string.save))
        }
    }
}

@Composable
fun UserProfile(
    name: String,
    surname: String,
    isDarkModeEnabled: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.padding(paddingBig)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(R.string.profile_icon),
                modifier = Modifier.size(profileIconSize),
            )

            Spacer(modifier = Modifier.width(spacerBig))

            Text(
                text = "$name $surname",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.dark_mode),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = isDarkModeEnabled,
                onCheckedChange = onToggleDarkMode,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = colorResource(R.color.white),
                    checkedTrackColor = colorResource(R.color.appBlueLight),
                    uncheckedTrackColor = colorResource(R.color.gray)
                )
            )
        }
    }
}

@Composable
@Preview
fun PreviewProfile() {
    UserProfile(
        name = "Marcelo",
        surname = "Gallardo",
        isDarkModeEnabled = false,
        onToggleDarkMode = { })
}

@Composable
@Preview
fun PreviewSetUsername() {
    SetUsername { _, _ -> }
}
