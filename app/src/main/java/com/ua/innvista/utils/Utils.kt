package com.ua.innvista.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.datastore.preferences.core.edit
import com.ua.innvista.data.PreferencesKeys
import com.ua.innvista.data.dataStore
import com.ua.innvista.data.getFromDataStore

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

