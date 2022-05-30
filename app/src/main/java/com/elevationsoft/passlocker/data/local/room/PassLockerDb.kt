package com.elevationsoft.passlocker.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.elevationsoft.passlocker.data.dto.CategoryDto
import com.elevationsoft.passlocker.data.dto.CredentialDto

@Database(entities = [CategoryDto::class, CredentialDto::class], version = 1, exportSchema = false)
abstract class PassLockerDb : RoomDatabase() {
    abstract fun roomDao(): RoomDao
}