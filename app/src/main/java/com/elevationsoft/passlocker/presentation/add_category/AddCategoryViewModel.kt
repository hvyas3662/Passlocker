package com.elevationsoft.passlocker.presentation.add_category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor() : ViewModel() {

    val isCategoryAdded: MutableLiveData<Boolean> = MutableLiveData()
    val isCategoryUpdated: MutableLiveData<Boolean> = MutableLiveData()

    fun validateCategoryName(catName: String): Boolean {
        return catName.trim().isNotEmpty()
    }

    fun getValidCategoryName(catName: String): String {
        return catName.trim()
    }

    fun validateCategoryPosition(position: String): Boolean {
        return position.trim().isNotEmpty()
    }

    fun getValidCategoryPosition(position: String): Int {
        return position.trim().toInt()
    }

    fun insertCategory(catName: String, position: Int) {

    }

    fun updateCategory(id: Long, catName: String, position: Int) {

    }
}