package com.elevationsoft.passlocker.presentation.add_category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.domain.use_cases.category.AddUpdateCategoryUC
import com.elevationsoft.passlocker.domain.use_cases.category.GetLastCategoryPositionUC
import com.elevationsoft.passlocker.utils.common_classes.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val addUpdateCategoryUC: AddUpdateCategoryUC,
    private val getLastCategoryPositionUC: GetLastCategoryPositionUC
) : ViewModel() {

    private val _screenState: MutableLiveData<AddCategoryScreenState> =
        MutableLiveData(AddCategoryScreenState())
    val screenState: LiveData<AddCategoryScreenState> = _screenState

    fun validateCategoryName(catName: String): Boolean {
        return catName.trim().isNotEmpty()
    }

    fun getValidCategoryName(catName: String): String {
        return catName.trim()
    }


    fun getLastCategoryPosition() {
        getLastCategoryPositionUC().onEach {
            when (it) {
                is DataState.Loading -> {
                    _screenState.value = screenState.value!!.copy(isLoading = it.isLoading)
                }

                is DataState.Success -> {
                    _screenState.value =
                        screenState.value!!.copy(nextCategoryPosition = (it.data!! + 1))
                }

                is DataState.Failed -> {
                    _screenState.value = screenState.value!!.copy(hasError = it.errorMsg)
                }
            }

        }.launchIn(viewModelScope)
    }


    fun insertUpdateCategory(id: Long, catName: String, position: Int) {
        addUpdateCategoryUC(Category(id, catName, position)).onEach {
            when (it) {
                is DataState.Loading -> {
                    _screenState.value = screenState.value!!.copy(isLoading = it.isLoading)
                }

                is DataState.Success -> {
                    _screenState.value = screenState.value!!.copy(isCategoryAdded = it.data!!)
                }

                is DataState.Failed -> {
                    _screenState.value = screenState.value!!.copy(hasError = it.errorMsg)
                }
            }

        }.launchIn(viewModelScope)
    }

    fun setEditMode(parcelableExtra: Category) {

    }


}