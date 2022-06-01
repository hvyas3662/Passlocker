package com.elevationsoft.passlocker.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elevationsoft.passlocker.data.dto.CategoryDto
import com.elevationsoft.passlocker.data.dto.CredentialDto

@Suppress("unused")
@Dao
interface RoomDao {

    //Category
    @Query("SELECT * FROM category ORDER BY position ASC")
    suspend fun getAllCategory(): List<CategoryDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateCategory(cat: CategoryDto)

    @Query("DELETE FROM category WHERE id = :categoryId")
    suspend fun deleteCategory(categoryId: Long)

    @Query("DELETE FROM credential WHERE categoryId = :catId")
    suspend fun deleteAllCredentialCategoryVise(catId: Long)

    @Query("SELECT MAX(position) FROM category")
    suspend fun getLastPosition(): Int?


    //Credential

    //fav vise
    @Query("SELECT * FROM credential WHERE isFavourite = 1 ORDER BY id DESC LIMIT :startIndex, :rowCount")
    suspend fun getFavCredentialPage(startIndex: Int, rowCount: Int): List<CredentialDto>

    @Query("SELECT * FROM credential WHERE isFavourite = 1 AND (title LIKE '%' || :search || '%' OR remark LIKE '%' || :search || '%') ORDER BY id DESC LIMIT :startIndex, :rowCount")
    suspend fun getFavCredentialPage(
        search: String,
        startIndex: Int,
        rowCount: Int
    ): List<CredentialDto>


    //category id vise
    @Query("SELECT * FROM credential WHERE categoryId = :catId ORDER BY id DESC LIMIT :startIndex, :rowCount")
    suspend fun getCredentialPage(
        catId: Long,
        startIndex: Int,
        rowCount: Int
    ): List<CredentialDto>

    @Query("SELECT * FROM credential WHERE  (title LIKE '%' || :search || '%' OR remark LIKE '%' || :search ||'%')  AND categoryId = :catId ORDER BY id DESC LIMIT :startIndex, :rowCount")
    suspend fun getCredentialPage(
        search: String,
        catId: Long,
        startIndex: Int,
        rowCount: Int
    ): List<CredentialDto>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateCredential(Credential: CredentialDto)

    @Query("DELETE FROM credential WHERE id = :credentialId")
    suspend fun deleteCredential(credentialId: Long)


}