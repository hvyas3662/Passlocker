package com.elevationsoft.passlocker.presentation.home_screen.passlist

import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.utils.common_classes.UiText

data class PassListFragmentState(
    val categoryList: List<Category> = mutableListOf(),
    val isCategoryLoaded: Boolean = false,
    val hasError: UiText = UiText.DynamicText(""),
    val isLoading: Boolean = true,
    val loadingType: Int = LOADING_TYPE_IN_UI
) {
    companion object {
        const val LOADING_TYPE_IN_UI = 0
        const val LOADING_TYPE_OVER_UI = 1
    }
}