package com.elevationsoft.passlocker.data.repository

import com.elevationsoft.passlocker.data.dto.CredentialDto
import com.elevationsoft.passlocker.data.local.room.RoomDao
import com.elevationsoft.passlocker.domain.repository.CredentialRepo
import javax.inject.Inject

class CredentialRepoImpl @Inject constructor(private val roomDao: RoomDao) : CredentialRepo {

    override suspend fun getFavCredentialPage(
        startIndex: Int,
        rowCount: Int
    ): List<CredentialDto> {
        return roomDao.getFavCredentialPage(startIndex, rowCount)
    }

    override suspend fun getCredentialPage(
        catId: Long,
        startIndex: Int,
        rowCount: Int
    ): List<CredentialDto> {
        return roomDao.getCredentialPage(catId, startIndex, rowCount)
    }

    override suspend fun getCredentialPage(
        search: String,
        catId: Long,
        startIndex: Int,
        rowCount: Int
    ): List<CredentialDto> {
        return roomDao.getCredentialPage(search, catId, startIndex, rowCount)
    }

    override suspend fun insertUpdateCredential(credential: CredentialDto) {
        return roomDao.insertUpdateCredential(credential)
    }

    override suspend fun deleteCredential(credentialId: Long) {
        return roomDao.deleteCredential(credentialId)
    }

    override suspend fun markUnMarkFavourite(credentialId: Long, fav: Boolean) {
        return roomDao.markUnMarkFavourite(credentialId, fav)
    }
}