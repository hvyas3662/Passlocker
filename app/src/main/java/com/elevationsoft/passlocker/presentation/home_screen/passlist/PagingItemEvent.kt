package com.elevationsoft.passlocker.presentation.home_screen.passlist

sealed class PagingItemEvent<T> {
    class EditItem<T>(val item: T) : PagingItemEvent<T>()
    class RemoveItem<T>(val item: T) : PagingItemEvent<T>()
    class RemoveAllItem<T> : PagingItemEvent<T>()
    class InsertUpdateItem<T>(val item: T) : PagingItemEvent<T>()
    class None<T> : PagingItemEvent<T>()
}
