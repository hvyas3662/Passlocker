package com.elevationsoft.passlocker.domain.use_cases.category

import com.elevationsoft.passlocker.domain.repository.CategoryRepo
import com.elevationsoft.passlocker.utils.common_classes.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateCategoryListPositionUC @Inject constructor(private val categoryRepo: CategoryRepo) {
    operator fun invoke(positionList: List<Long>): Flow<DataState<Boolean>> {
        return flow {
            categoryRepo.updateCategoryListPosition(positionList)
            emit(DataState.Success(true))
        }
    }
}