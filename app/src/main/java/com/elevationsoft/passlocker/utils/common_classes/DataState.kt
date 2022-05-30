package com.elevationsoft.passlocker.utils.common_classes

sealed class DataState<T>(
    private val data: T? = null,
    private val errorMsg: UiText = UiText.DynamicText(""),
    private val isLoading: Boolean = false
) {
    class Success<T>(data: T) : DataState<T>(data = data)
    class Failed<T>(errorMsg: UiText) : DataState<T>(errorMsg = errorMsg)
    class Loading<T>(isLoading: Boolean) : DataState<T>(isLoading = isLoading)
}
