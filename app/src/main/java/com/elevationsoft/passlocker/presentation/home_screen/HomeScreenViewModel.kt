package com.elevationsoft.passlocker.presentation.home_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elevationsoft.passlocker.domain.use_cases.category.GetCategoryListUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(categoryListUC: GetCategoryListUC) : ViewModel() {

    private val _screenState: MutableLiveData<HomeScreenStatus> =
        MutableLiveData(HomeScreenStatus())
    val screenState: LiveData<HomeScreenStatus> = _screenState

    fun updateSelectedScreen(selectedScreen: Int) {
        _screenState.value = screenState.value?.copy(selectedScreen = selectedScreen)
    }

}