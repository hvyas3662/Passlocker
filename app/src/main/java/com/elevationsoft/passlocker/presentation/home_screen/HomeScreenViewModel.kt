package com.elevationsoft.passlocker.presentation.home_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elevationsoft.passlocker.domain.use_cases.category.GetCategoryListUC
import com.elevationsoft.passlocker.utils.common_classes.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val categoryListUC: GetCategoryListUC) :
    ViewModel() {

    private val _screenState: MutableLiveData<HomeScreenState> =
        MutableLiveData(HomeScreenState())
    val screenState: LiveData<HomeScreenState> = _screenState

    fun updateSelectedScreen(selectedScreen: Int) {
        _screenState.value = screenState.value?.copy(selectedScreen = selectedScreen)
    }

    fun getCategoryList() {
        categoryListUC().onEach {
            when (it) {
                is DataState.Failed -> {
                    _screenState.value = screenState.value?.copy(
                        categoryListStatus = screenState.value?.categoryListStatus!!.copy(
                            error = it.errorMsg,
                        )
                    )


                }
                is DataState.Loading -> {
                    _screenState.value = screenState.value?.copy(
                        categoryListStatus = screenState.value?.categoryListStatus!!.copy(
                            loading = it.isLoading
                        )
                    )
                }
                is DataState.Success -> {
                    _screenState.value = it.data?.let { list ->
                        screenState.value?.copy(
                            categoryList = list,
                            categoryListStatus = screenState.value?.categoryListStatus!!.copy(
                                categoryList = list
                            )
                        )
                    }
                }
            }

        }.launchIn(viewModelScope)
    }

}