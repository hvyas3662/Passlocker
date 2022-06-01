package com.elevationsoft.passlocker.domain.repository

import com.elevationsoft.passlocker.data.dto.CategoryDto

interface CategoryRepo {
    suspend fun getAllCategory(): List<CategoryDto>

    suspend fun insertUpdateCategory(cat: CategoryDto)

    suspend fun deleteCategory(categoryId: Long)

    suspend fun getLastPosition(): Int?
}