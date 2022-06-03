package com.elevationsoft.passlocker.presentation.home_screen.passlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.elevationsoft.passlocker.domain.models.Credential
import com.elevationsoft.passlocker.domain.use_cases.category.GetCategoryListUC
import com.elevationsoft.passlocker.domain.use_cases.credential.DeleteCredentialUC
import com.elevationsoft.passlocker.domain.use_cases.credential.GetCredentialListUC
import com.elevationsoft.passlocker.domain.use_cases.credential.MarkUnMarkFavouriteCredentialUC
import com.elevationsoft.passlocker.domain.utils.CredentialListMode
import com.elevationsoft.passlocker.presentation.home_screen.passlist.PassListFragmentState.Companion.LOADING_TYPE_OVER_UI
import com.elevationsoft.passlocker.utils.PrefUtils
import com.elevationsoft.passlocker.utils.common_classes.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PasslistFragmentViewModel @Inject constructor(
    private val prefUtils: PrefUtils,
    private val categoryListUC: GetCategoryListUC,
    private val markUnMarkFavouriteCredentialUC: MarkUnMarkFavouriteCredentialUC,
    private val deleteCredentialUC: DeleteCredentialUC,
    private val getCredentialListUC: GetCredentialListUC
) : ViewModel() {

    private val _passlistFragState: MutableLiveData<PassListFragmentState> =
        MutableLiveData(PassListFragmentState())
    val passlistFragState: LiveData<PassListFragmentState> = _passlistFragState

    val isItemMarkedFav: MutableLiveData<Credential> = MutableLiveData()

    val isItemDeleted: MutableLiveData<Credential> = MutableLiveData()

    private var credentialPagingData: Flow<PagingData<Credential>>? = null


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
                    _passlistFragState.value = passlistFragState.value?.copy(
                        hasError = it.errorMsg,
                    )
                }

                is DataState.Success -> {
                    isItemMarkedFav.value = it.data
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
                    _passlistFragState.value = passlistFragState.value?.copy(
                        hasError = it.errorMsg,
                    )
                }

                is DataState.Success -> {
                    isItemDeleted.value = it.data
                }
            }
        }.launchIn(viewModelScope)
    }


    fun getCredentialList(searchQuery: String, categoryId: Long): Flow<PagingData<Credential>> {
        val listMode = if (categoryId <= 0) {
            if (searchQuery.isNotEmpty()) {
                CredentialListMode.FavouriteSearch(searchQuery)
            } else {
                CredentialListMode.Favourite()
            }
        } else {
            if (searchQuery.isNotEmpty()) {
                CredentialListMode.CategorySearch(searchQuery, categoryId)
            } else {
                CredentialListMode.Category(categoryId)
            }
        }
        credentialPagingData = getCredentialListUC(listMode)
            .cachedIn(viewModelScope)
        return credentialPagingData!!
    }

    fun updateItemInPaging(pagingItemEvent: PagingItemEvent<Credential>): Flow<PagingData<Credential>>? {
        credentialPagingData?.let {
            credentialPagingData = applyModification(it, pagingItemEvent)
            return credentialPagingData
        }
        return null
    }

    private fun applyModification(
        pagingDataFlow: Flow<PagingData<Credential>>,
        pagingItemEvent: PagingItemEvent<Credential>
    ): Flow<PagingData<Credential>> {
        return pagingDataFlow.map { pagingData ->
            when (pagingItemEvent) {
                is PagingItemEvent.None -> {
                    pagingData
                }
                is PagingItemEvent.EditItem -> {
                    pagingData.map pageDataMap@{
                        if (it.id == pagingItemEvent.item.id) {
                            return@pageDataMap pagingItemEvent.item.copy()
                        }
                        return@pageDataMap it
                    }
                }
                is PagingItemEvent.RemoveItem -> {
                    pagingData.filter { it.id != pagingItemEvent.item.id }
                }
                is PagingItemEvent.RemoveAllItem -> {
                    pagingData.filter { it.id == -1L }
                }
                is PagingItemEvent.InsertUpdateItem -> {
                    pagingData.insertHeaderItem(item = pagingItemEvent.item.copy())
                }
            }
        }

    }

}