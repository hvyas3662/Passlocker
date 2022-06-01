package com.elevationsoft.passlocker.presentation.home_screen.category

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
class CategoryFragmentViewModel @Inject constructor(
    private val categoryListUC: GetCategoryListUC
) : ViewModel() {

    private val _categoryFragState: MutableLiveData<CategoryFragmentState> =
        MutableLiveData(CategoryFragmentState())
    val categoryFragState: LiveData<CategoryFragmentState> = _categoryFragState


    fun getCategoryList() {
        categoryListUC().onEach {
            when (it) {
                is DataState.Loading -> {
                    _categoryFragState.value = categoryFragState.value?.copy(
                        isLoading = it.isLoading
                    )
                }

                is DataState.Failed -> {
                    _categoryFragState.value = categoryFragState.value?.copy(
                        hasError = it.errorMsg
                    )
                }

                is DataState.Success -> {
                    _categoryFragState.value = categoryFragState.value?.copy(
                        categoryList = it.data ?: mutableListOf()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}