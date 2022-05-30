package com.elevationsoft.passlocker.presentation.home_screen

import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.utils.common_classes.UiText

data class HomeScreenStatus(
    val selectedScreen: Int = 0,
    val categoryList: List<Category> = ArrayList(),
    val passListStatus: PassListState = PassListState(),
    val categoryListStatus: CategoryState = CategoryState(categoryList),
    val reloadCategory: Boolean = false,
    val reloadPassList: Boolean = false
)

data class PassListState(
    val selectedCategoryIndex: Int = 0,
    val error: UiText = UiText.DynamicText(""),
    val loading: Boolean = true
)

data class CategoryState(
    val categoryList: List<Category>,
    val error: UiText = UiText.DynamicText(""),
    val loading: Boolean = true
)