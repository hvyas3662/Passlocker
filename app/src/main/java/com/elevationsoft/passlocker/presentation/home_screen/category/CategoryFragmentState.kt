package com.elevationsoft.passlocker.presentation.home_screen.category

import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.utils.common_classes.UiText

data class CategoryFragmentState(
    val categoryList: List<Category> = mutableListOf(),
    val hasError: UiText = UiText.DynamicText(""),
    val isLoading: Boolean = true
)