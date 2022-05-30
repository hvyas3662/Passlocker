package com.elevationsoft.passlocker.domain.models

data class Credential(
    val id: Long,
    val title: String,
    val userName: String,
    val password: String,
    val remark: String,
    val isFavourite: Boolean,
    val categoryId: Long
)
