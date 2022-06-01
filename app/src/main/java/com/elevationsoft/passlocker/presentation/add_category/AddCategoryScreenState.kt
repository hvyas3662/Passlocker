package com.elevationsoft.passlocker.presentation.add_category

import com.elevationsoft.passlocker.utils.common_classes.UiText

data class AddCategoryScreenState(
    val isCategoryAdded: Boolean = false,
    val nextCategoryPosition: Int = 0,
    val error: UiText = UiText.DynamicText(""),
    val isLoading: Boolean = false
)