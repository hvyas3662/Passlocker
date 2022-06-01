package com.elevationsoft.passlocker.presentation.home_screen.passlist

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
class PasslistFragmentViewModel @Inject constructor(
    private val categoryListUC: GetCategoryListUC
) : ViewModel() {

    private val _passlistFragState: MutableLiveData<PassListFragmentState> =
        MutableLiveData(PassListFragmentState())
    val passlistFragState: LiveData<PassListFragmentState> = _passlistFragState


    fun getCategoryList() {
        categoryListUC().onEach {
            when (it) {
                is DataState.Loading -> {
                    _passlistFragState.value = passlistFragState.value?.copy(
                        isLoading = it.isLoading
                    )
                }

                is DataState.Failed -> {
                    _passlistFragState.value = passlistFragState.value?.copy(
                        hasError = it.errorMsg
                    )
                }

                is DataState.Success -> {
                    _passlistFragState.value = passlistFragState.value?.copy(
                        categoryList = it.data ?: mutableListOf()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }


}