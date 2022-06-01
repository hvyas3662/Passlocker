package com.elevationsoft.passlocker.domain.use_cases.category

import com.elevationsoft.passlocker.data.repository.CategoryRepoImpl
import com.elevationsoft.passlocker.utils.common_classes.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateCategoryListPositionUC @Inject constructor(private val categoryRepo: CategoryRepoImpl) {
    operator fun invoke(positionList: List<Long>): Flow<DataState<Boolean>> {
        return flow {
            categoryRepo.updateCategoryListPosition(positionList)
            emit(DataState.Success(true))
        }
    }
}