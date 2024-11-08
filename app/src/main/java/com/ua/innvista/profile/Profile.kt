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
import androidx.compose.material3.HorizontalDivider
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
import com.ua.innvista.ui.theme.padding
import com.ua.innvista.ui.theme.paddingBig
import com.ua.innvista.ui.theme.profileIconSize
import com.ua.innvista.ui.theme.spacer

@Composable
fun Profile() {
    val viewModel = hiltViewModel<ProfileViewModel>()
    val name by viewModel.name.collectAsState()
    val surname by viewModel.surname.collectAsState()
    val isDarkModeEnabled by viewModel.isDarkModeEnabled.collectAsState()

    if (name.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        )
        {
            SetUsername(
                onSave = { enteredName, enteredSurname ->
                    viewModel.saveUser(
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
            onToggleDarkMode = { enabled ->
                viewModel.toggleDarkMode(enabled)
            }
        )
    }
}

@Composable
fun SetUsername(
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    val isFormValid = name.isNotBlank() && surname.isNotBlank()
    var hasStartedTyping by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingBig),
        verticalArrangement = Arrangement.spacedBy(paddingBig, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.profile_prompt),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = padding)
        )

        TextField(
            value = name,
            onValueChange = {
                name = it
                if (it.isNotEmpty()) hasStartedTyping = true
            },
            label = { Text(text = stringResource(R.string.name)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = surname,
            onValueChange = {
                surname = it
                if (it.isNotEmpty()) hasStartedTyping = true
            },
            label = { Text(text = stringResource(R.string.surname)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { onSave(name, surname) },
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingBig),
        ) {
            Text(text = stringResource(R.string.save))
        }

        if (!isFormValid && hasStartedTyping) {
            Text(
                text = stringResource(R.string.fill_all_fields),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = padding)
            )
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
    var isToggled by remember { mutableStateOf(isDarkModeEnabled) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingBig),
        verticalArrangement = Arrangement.spacedBy(paddingBig, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = paddingBig),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(R.string.profile_icon),
                modifier = Modifier.size(profileIconSize),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(spacer))

            Text(
                text = "$name $surname",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = padding)
        )

        // Dark Mode Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.dark_mode),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onBackground
            )

            Switch(
                checked = isToggled,
                onCheckedChange = { enabled ->
                    isToggled = enabled
                    onToggleDarkMode(enabled)
                },
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
