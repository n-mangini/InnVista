package com.ua.innvista.wishlist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.ua.innvista.data.AppDatabase
import com.ua.innvista.hotel.HotelModel
import com.ua.innvista.hotel.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {
    private val appDatabase = AppDatabase.getDatabase(context)

    val wishlist = appDatabase.hotelDao().getAllHotels().asFlow()

    fun addHotel(hotelModel: HotelModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val hotelEntity = hotelModel.toEntity()
            appDatabase.hotelDao().insert(hotelEntity)
        }
    }

    fun deleteHotel(hotelId: Long) {
        viewModelScope.launch {
            appDatabase.hotelDao().deleteById(hotelId)
        }
    }

    fun isHotelInWishlist(hotelId: Long, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isInWishlist = appDatabase.hotelDao().getById(hotelId) != null
            onResult(isInWishlist)
        }
    }
}
