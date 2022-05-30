package com.elevationsoft.passlocker.domain.utils

sealed class CredentialListMode() {
    class Favourite(
        startIndex: Int,
        rowCount: Int
    ) : CredentialListMode()

    class FavouriteSearch(
        searchStr: String,
        startIndex: Int,
        rowCount: Int
    ) : CredentialListMode()

    class Category(
        categoryId: Long,
        startIndex: Int,
        rowCount: Int
    ) : CredentialListMode()

    class CategorySearch(
        searchStr: String,
        categoryId: Long,
        startIndex: Int,
        rowCount: Int
    ) : CredentialListMode()
}
