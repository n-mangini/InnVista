package com.ua.innvista.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ua.innvista.data.AppDatabase.Companion.clearDatabase
import com.ua.innvista.data.PreferencesKeys
import com.ua.innvista.data.PreferencesKeys.DARK_MODE_KEY
import com.ua.innvista.data.clearDataStore
import com.ua.innvista.data.getFromDataStore
import com.ua.innvista.data.saveToDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {

    private var _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private var _surname = MutableStateFlow("")
    val surname = _surname.asStateFlow()

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)

    private var _isDarkModeEnabled = MutableStateFlow(false)
    val isDarkModeEnabled = _isDarkModeEnabled.asStateFlow()

    init {
        loadDarkMode()
        loadUser()
        loadLoggedIn()
    }

    private fun loadDarkMode() {
        viewModelScope.launch {
            getFromDataStore(context, DARK_MODE_KEY).collect { isDarkMode ->
                _isDarkModeEnabled.value = isDarkMode == true
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            getFromDataStore(context, PreferencesKeys.NAME_KEY).collect {
                _name.value = it ?: ""
            }
            getFromDataStore(context, PreferencesKeys.SURNAME_KEY).collect {
                _surname.value = it ?: ""
            }
        }
    }

    private fun loadLoggedIn() {
        viewModelScope.launch {
            getFromDataStore(context, PreferencesKeys.NAME_KEY).collect {
                _isLoggedIn.value = it != null
            }
        }
    }

    fun saveUser(name: String, surname: String) {
        viewModelScope.launch {
            saveToDataStore(
                context,
                name,
                PreferencesKeys.NAME_KEY
            )
            saveToDataStore(
                context,
                surname,
                PreferencesKeys.SURNAME_KEY
            )
            _name.value = name
            _surname.value = surname
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            saveToDataStore(context, enabled, DARK_MODE_KEY)
            _isDarkModeEnabled.value = enabled
        }
    }

    suspend fun logout(context: Context) {
        clearDataStore(context)
        clearDatabase(context)
    }
}
