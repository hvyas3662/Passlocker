package com.elevationsoft.passlocker.presentation.add_credentials

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.domain.models.Credential
import com.elevationsoft.passlocker.domain.use_cases.category.GetCategoryListUC
import com.elevationsoft.passlocker.domain.use_cases.credential.AddUpdateCredentialUC
import com.elevationsoft.passlocker.utils.common_classes.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddCredentialViewModel @Inject constructor(
    private val categoryListUC: GetCategoryListUC,
    private val addUpdateCredentialUC: AddUpdateCredentialUC
) : ViewModel() {

    private val _screenState: MutableLiveData<AddCredentialScreenState> =
        MutableLiveData(AddCredentialScreenState())
    val screenState: LiveData<AddCredentialScreenState> = _screenState

    fun getCategoryList() {
        categoryListUC().onEach {
            when (it) {
                is DataState.Loading -> {
                    _screenState.value = screenState.value?.copy(
                        isLoading = it.isLoading
                    )
                }

                is DataState.Failed -> {
                    _screenState.value = screenState.value?.copy(
                        hasError = it.errorMsg
                    )
                }

                is DataState.Success -> {
                    val finalList: MutableList<Category> = mutableListOf()
                    val unFilteredList = it.data ?: mutableListOf()
                    unFilteredList.forEach { cat ->
                        if (cat.id > 0) {
                            finalList.add(cat)
                        }
                    }
                    _screenState.value = screenState.value?.copy(
                        categoryList = finalList,
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setCategoryLoaded() {
        _screenState.value = screenState.value?.copy(
            isCategoryLoaded = true
        )
    }


    fun validateTitle(title: String): Boolean {
        return title.trim().isNotEmpty()
    }

    fun getValidTitle(title: String): String {
        return title.trim()
    }

    fun validateUsername(username: String): Boolean {
        return username.trim().isNotEmpty()
    }

    fun getValidUsername(username: String): String {
        return username.trim()
    }

    fun getValidPassword(password: String): String {
        return password.trim()
    }

    fun getValidRemark(remark: String): String {
        return remark.trim()
    }

    fun insertUpdateCredential(
        id: Long,
        title: String,
        username: String,
        password: String,
        remark: String,
        favourite: Boolean,
        catId: Long
    ) {
        val credential = Credential(id, title, username, password, remark, favourite, catId)
        addUpdateCredentialUC(credential).onEach {
            when (it) {
                is DataState.Loading -> {
                    _screenState.value = screenState.value!!.copy(isLoading = it.isLoading)
                }

                is DataState.Success -> {
                    _screenState.value =
                        screenState.value!!.copy(isCredentialAdded = true, credential = it.data)
                }

                is DataState.Failed -> {
                    _screenState.value = screenState.value!!.copy(hasError = it.errorMsg)
                }
            }
        }.launchIn(viewModelScope)

    }

}