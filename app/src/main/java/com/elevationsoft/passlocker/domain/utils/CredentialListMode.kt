package com.elevationsoft.passlocker.domain.utils

sealed class CredentialListMode(
    val searchStr: String = "",
    val categoryId: Long = -1L
) {
    class Favourite : CredentialListMode()

    class FavouriteSearch(
        searchStr: String
    ) : CredentialListMode(searchStr = searchStr)

    class Category(
        categoryId: Long,
    ) : CredentialListMode(categoryId = categoryId)

    class CategorySearch(
        searchStr: String,
        categoryId: Long,
    ) : CredentialListMode(searchStr = searchStr, categoryId = categoryId)
}
