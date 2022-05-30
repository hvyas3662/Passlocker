package com.elevationsoft.passlocker.domain.use_cases.category

import com.elevationsoft.passlocker.data.repository.CategoryRepoImpl
import com.elevationsoft.passlocker.utils.common_classes.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteCategoryUC @Inject constructor(private val categoryRepo: CategoryRepoImpl) {

    operator fun invoke(categoryId: Long): Flow<DataState<Boolean>> {
        return flow {
            emit(DataState.Loading(true))
            withContext(Dispatchers.IO) {
                delay(2000)
                categoryRepo.deleteCategory(categoryId)
                emit(DataState.Success(true))
            }
            emit(DataState.Loading(false))
        }
    }
}