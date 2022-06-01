package com.elevationsoft.passlocker.data.repository

import com.elevationsoft.passlocker.data.dto.CategoryDto
import com.elevationsoft.passlocker.data.local.room.RoomDao
import com.elevationsoft.passlocker.domain.repository.CategoryRepo
import javax.inject.Inject

class CategoryRepoImpl @Inject constructor(private val roomDao: RoomDao) : CategoryRepo {
    override suspend fun getAllCategory(): List<CategoryDto> {
        return roomDao.getAllCategory()
    }

    override suspend fun insertUpdateCategory(cat: CategoryDto) {
        roomDao.insertUpdateCategory(cat)
    }

    override suspend fun deleteCategory(categoryId: Long) {
        roomDao.deleteCategory(categoryId)
        roomDao.deleteAllCredentialCategoryVise(categoryId)
    }

    override suspend fun getLastPosition(): Int? {
        return roomDao.getLastPosition()
    }

}