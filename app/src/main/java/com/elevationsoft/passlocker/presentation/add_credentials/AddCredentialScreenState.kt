package com.elevationsoft.passlocker.presentation.add_credentials

import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.utils.common_classes.UiText

data class AddCredentialScreenState(
    val isCategoryLoaded: Boolean = false,
    val categoryList: List<Category> = mutableListOf(),
    val isCredentialAdded: Boolean = false,
    val hasError: UiText = UiText.DynamicText(""),
    val isLoading: Boolean = false,
    val loadingType: Int = LOADING_TYPE_OVER_UI
) {
    companion object {
        const val LOADING_TYPE_IN_UI = 0
        const val LOADING_TYPE_OVER_UI = 1
    }
}