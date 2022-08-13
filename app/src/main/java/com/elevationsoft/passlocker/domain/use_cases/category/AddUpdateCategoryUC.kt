package com.elevationsoft.passlocker.domain.use_cases.category

import com.elevationsoft.passlocker.data.dto.CategoryDto.Companion.toCategoryDto
import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.domain.repository.CategoryRepo
import com.elevationsoft.passlocker.utils.common_classes.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddUpdateCategoryUC @Inject constructor(private val categoryRepo: CategoryRepo) {

    operator fun invoke(category: Category): Flow<DataState<Boolean>> {
        return flow {
            emit(DataState.Loading(true))
            categoryRepo.insertUpdateCategory(category.toCategoryDto())
            emit(DataState.Success(true))
            emit(DataState.Loading(false))
        }
    }
}