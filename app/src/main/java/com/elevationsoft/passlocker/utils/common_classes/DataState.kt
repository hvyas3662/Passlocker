package com.elevationsoft.passlocker.utils.common_classes

sealed class DataState<T>(
    val data: T? = null,
    val errorMsg: UiText = UiText.DynamicText(""),
    val isLoading: Boolean = false
) {
    class Success<T>(data: T) : DataState<T>(data = data)
    class Failed<T>(errorMsg: UiText) : DataState<T>(errorMsg = errorMsg)
    class Loading<T>(isLoading: Boolean) : DataState<T>(isLoading = isLoading)
}
