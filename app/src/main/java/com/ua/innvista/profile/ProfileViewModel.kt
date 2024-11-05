package com.ua.innvista.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ua.innvista.data.PreferencesKeys
import com.ua.innvista.data.getFromDataStore
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

    init {
        getUserFromDataStore()
    }

    private fun getUserFromDataStore() {
        viewModelScope.launch {
            getFromDataStore(context, PreferencesKeys.NAME_KEY).collect {
                _name.value = it ?: ""
            }
            getFromDataStore(context, PreferencesKeys.SURNAME_KEY).collect {
                _surname.value = it ?: ""
            }
        }
    }

    fun saveUserToDataStore(name: String, surname: String) {
        viewModelScope.launch {
            com.ua.innvista.data.saveToDataStore(
                context,
                name,
                PreferencesKeys.NAME_KEY
            )
            com.ua.innvista.data.saveToDataStore(
                context,
                surname,
                PreferencesKeys.SURNAME_KEY
            )
            _surname.value = name
        }
    }
}
