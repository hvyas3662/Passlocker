package com.elevationsoft.passlocker.domain.use_cases.category

import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.domain.repository.CategoryRepo
import com.elevationsoft.passlocker.utils.common_classes.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCategoryListUC @Inject constructor(private val categoryRepo: CategoryRepo) {
    operator fun invoke(): Flow<DataState<List<Category>>> {
        return flow {
            emit(DataState.Loading(true))
            val catList = categoryRepo.getAllCategory().map {
                it.toCategory()
            }
            val resultCatList = ArrayList<Category>()
            resultCatList.add(Category(-1L, "Favourite", -1))
            resultCatList.addAll(catList)
            emit(DataState.Success(resultCatList))
            emit(DataState.Loading(false))
        }
    }
}