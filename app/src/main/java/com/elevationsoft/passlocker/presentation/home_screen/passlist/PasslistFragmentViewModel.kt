package com.elevationsoft.passlocker.presentation.home_screen.passlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elevationsoft.passlocker.R
import com.elevationsoft.passlocker.domain.use_cases.category.GetCategoryListUC
import com.elevationsoft.passlocker.domain.use_cases.credential.DeleteCredentialUC
import com.elevationsoft.passlocker.domain.use_cases.credential.MarkUnMarkFavouriteCredentialUC
import com.elevationsoft.passlocker.presentation.home_screen.passlist.PassListFragmentState.Companion.LOADING_TYPE_OVER_UI
import com.elevationsoft.passlocker.utils.PrefUtils
import com.elevationsoft.passlocker.utils.common_classes.DataState
import com.elevationsoft.passlocker.utils.common_classes.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PasslistFragmentViewModel @Inject constructor(
    private val prefUtils: PrefUtils,
    private val categoryListUC: GetCategoryListUC,
    private val markUnMarkFavouriteCredentialUC: MarkUnMarkFavouriteCredentialUC,
    private val deleteCredentialUC: DeleteCredentialUC
) : ViewModel() {

    private val _passlistFragState: MutableLiveData<PassListFragmentState> =
        MutableLiveData(PassListFragmentState())
    val passlistFragState: LiveData<PassListFragmentState> = _passlistFragState

    val isItemMarkedFav: MutableLiveData<UiText> = MutableLiveData()

    val isItemDeleted: MutableLiveData<UiText> = MutableLiveData()

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

    fun setCategoryLoaded() {
        _passlistFragState.value = passlistFragState.value?.copy(
            isCategoryLoaded = true
        )
    }

    fun saveSelectedCategoryId(id: Long) {
        prefUtils.saveSelectedCategory(id)
    }

    fun getSavedSelectedCategoryId(): Long {
        return prefUtils.getSavedSelectedCategoryId()
    }

    fun markUnMarkFavourite(credentialId: Long, fav: Boolean) {
        markUnMarkFavouriteCredentialUC(credentialId, fav).onEach {
            when (it) {
                is DataState.Loading -> {
                    _passlistFragState.value = passlistFragState.value?.copy(
                        isLoading = it.isLoading,
                        loadingType = LOADING_TYPE_OVER_UI
                    )
                }

                is DataState.Failed -> {
                    isItemMarkedFav.value = it.errorMsg
                }

                is DataState.Success -> {
                    if (fav) {
                        isItemMarkedFav.value = UiText.StaticText(R.string.text_item_marked_fav)
                    } else {
                        isItemMarkedFav.value = UiText.StaticText(R.string.text_item_unmarked_fav)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteCredential(credentialId: Long) {
        deleteCredentialUC(credentialId).onEach {
            when (it) {
                is DataState.Loading -> {
                    _passlistFragState.value = passlistFragState.value?.copy(
                        isLoading = it.isLoading,
                        loadingType = LOADING_TYPE_OVER_UI
                    )
                }

                is DataState.Failed -> {
                    isItemDeleted.value = it.errorMsg
                }

                is DataState.Success -> {
                    isItemDeleted.value = UiText.StaticText(R.string.text_credential_deleted_msg)
                }
            }
        }.launchIn(viewModelScope)
    }


}