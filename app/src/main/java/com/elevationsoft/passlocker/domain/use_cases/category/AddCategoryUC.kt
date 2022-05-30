package com.elevationsoft.passlocker.domain.use_cases.category

import com.elevationsoft.passlocker.data.dto.CategoryDto.Companion.toCategoryDto
import com.elevationsoft.passlocker.data.repository.CategoryRepoImpl
import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.utils.common_classes.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddCategoryUC @Inject constructor(private val categoryRepo: CategoryRepoImpl) {

    operator fun invoke(category: Category): Flow<DataState<Boolean>> {
        return flow {
            emit(DataState.Loading(true))
            withContext(Dispatchers.IO) {
                delay(2000)
                categoryRepo.insertUpdateCategory(category.toCategoryDto())
                emit(DataState.Success(true))
            }
            emit(DataState.Loading(false))
        }
    }
}