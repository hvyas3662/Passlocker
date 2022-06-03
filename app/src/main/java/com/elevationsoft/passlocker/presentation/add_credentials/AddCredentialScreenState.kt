package com.elevationsoft.passlocker.presentation.add_credentials

import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.domain.models.Credential
import com.elevationsoft.passlocker.utils.common_classes.UiText

data class AddCredentialScreenState(
    val isCategoryLoaded: Boolean = false,
    val categoryList: List<Category> = mutableListOf(),
    val isCredentialAdded: Boolean = false,
    val credential: Credential? = null,
    val hasError: UiText = UiText.DynamicText(""),
    val isLoading: Boolean = false,
)