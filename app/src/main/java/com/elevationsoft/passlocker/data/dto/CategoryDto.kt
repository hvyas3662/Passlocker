package com.elevationsoft.passlocker.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elevationsoft.passlocker.domain.models.Category

@Suppress("unused")
@Entity(tableName = "category")
class CategoryDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val categoryName: String,
    val position: Int
) {
    fun toCategory(): Category {
        return Category(id, categoryName, position)
    }

    companion object {
        fun Category.toCategoryDto(): CategoryDto {
            return CategoryDto(
                this.id,
                this.categoryName,
                this.position
            )
        }
    }
}
