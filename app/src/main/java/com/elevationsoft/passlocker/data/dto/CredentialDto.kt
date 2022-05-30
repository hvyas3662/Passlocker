package com.elevationsoft.passlocker.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elevationsoft.passlocker.domain.models.Credential

@Suppress("unused")
@Entity(tableName = "credential")
class CredentialDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val userName: String,
    val password: String,
    val remark: String,
    val isFavourite: Boolean,
    val categoryId: Long
) {
    fun toCredential(): Credential {
        return Credential(id, title, userName, password, remark, isFavourite, categoryId)
    }


    companion object {
        fun Credential.toCredentialDto(): CredentialDto {
            return CredentialDto(
                this.id,
                this.title,
                this.userName,
                this.password,
                this.remark,
                this.isFavourite,
                this.categoryId
            )
        }
    }
}
