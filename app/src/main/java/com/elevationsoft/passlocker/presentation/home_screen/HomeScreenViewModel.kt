package com.elevationsoft.passlocker.presentation.home_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor() : ViewModel() {

    private val _homeScreenState: MutableLiveData<HomeScreenState> =
        MutableLiveData(HomeScreenState())
    val homeScreenState: LiveData<HomeScreenState> = _homeScreenState


    fun updateSelectedScreen(selectedScreen: Int) {
        _homeScreenState.value = homeScreenState.value?.copy(selectedScreen = selectedScreen)
    }

    companion object {
        const val PASSLIST_TAB_INDEX = 0
        const val CATEGORY_TAB_INDEX = 1
    }
}