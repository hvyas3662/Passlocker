package com.elevationsoft.passlocker.data.repository

import com.elevationsoft.passlocker.data.dto.CategoryDto
import com.elevationsoft.passlocker.data.local.room.RoomDao
import com.elevationsoft.passlocker.domain.repository.CategoryRepo
import kotlinx.coroutines.delay
import javax.inject.Inject

class CategoryRepoImpl @Inject constructor(private val roomDao: RoomDao) : CategoryRepo {
    override suspend fun getAllCategory(): List<CategoryDto> {
        return roomDao.getAllCategory()
    }

    override suspend fun insertUpdateCategory(cat: CategoryDto) {
        delay(500)
        roomDao.insertUpdateCategory(cat)
    }

    override suspend fun deleteCategory(categoryId: Long) {
        delay(500)
        roomDao.deleteCategory(categoryId)
        roomDao.deleteAllCredentialCategoryVise(categoryId)
    }

    override suspend fun getLastPosition(): Int? {
        return roomDao.getLastPosition()
    }

    override suspend fun updateCategoryListPosition(catPositionList: List<Long>) {
        catPositionList.forEachIndexed { index, id ->
            roomDao.updatePositionViaId(id, index)
        }
    }

}