package com.elevationsoft.passlocker.domain.use_cases.category

import com.elevationsoft.passlocker.data.repository.CategoryRepoImpl
import com.elevationsoft.passlocker.utils.common_classes.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLastCategoryPositionUC @Inject constructor(private val categoryRepo: CategoryRepoImpl) {

    operator fun invoke(): Flow<DataState<Int>> {
        return flow {
            emit(DataState.Loading(true))
            val lastPos: Int = categoryRepo.getLastPosition() ?: 0
            emit(DataState.Success(lastPos))
            emit(DataState.Loading(false))
        }
    }
}