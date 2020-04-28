package com.example.sunnyweather.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.dao.PlaceDao

class SettingsViewModel : ViewModel() {

    private val _isChecked = MutableLiveData<Boolean>()

    val isChecked: LiveData<Boolean>
        get() = _isChecked

    fun saveUpdate(isChecked: Boolean) {
        PlaceDao.saveUpdate(isChecked)
        _isChecked.value = isChecked
    }

    fun getUpdate() = PlaceDao.getSavedUpdate()

    fun isSavedUpdate() = PlaceDao.isUpdateSaved()
}