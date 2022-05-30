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
        return roomDao.insertUpdateCategory(cat)
    }

    override suspend fun deleteCategory(categoryId: Long) {
        return roomDao.deleteCategory(categoryId)
    }
}