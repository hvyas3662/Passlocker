package com.elevationsoft.passlocker.presentation.home_screen.passlist

import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.utils.common_classes.UiText

data class PassListFragmentState(
    val categoryList: List<Category> = mutableListOf(),
    val selectedCategoryId: Long = -1L,
    val isCategoryLoaded: Boolean = false,
    val hasError: UiText = UiText.DynamicText(""),
    val isLoading: Boolean = true
)