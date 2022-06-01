package com.elevationsoft.passlocker.domain.use_cases.category

import com.elevationsoft.passlocker.data.repository.CategoryRepoImpl
import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.utils.common_classes.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteCategoryUC @Inject constructor(private val categoryRepo: CategoryRepoImpl) {

    operator fun invoke(categoryId: Long): Flow<DataState<List<Category>>> {
        return flow {
            emit(DataState.Loading(true))
            categoryRepo.deleteCategory(categoryId)
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