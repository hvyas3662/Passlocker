package com.elevationsoft.passlocker.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.elevationsoft.passlocker.data.dto.CredentialDto
import com.elevationsoft.passlocker.data.local.room.RoomDao
import com.elevationsoft.passlocker.data.paging_data_source.CredentialDataSource
import com.elevationsoft.passlocker.domain.repository.CredentialRepo
import com.elevationsoft.passlocker.domain.utils.CredentialListMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CredentialRepoImpl @Inject constructor(private val roomDao: RoomDao) : CredentialRepo {
    override fun getCredentialPage(
        listMode: CredentialListMode
    ): Flow<PagingData<CredentialDto>> {
        return Pager(
            PagingConfig(pageSize = 10, enablePlaceholders = false),
            initialKey = 1
        ) {
            CredentialDataSource(roomDao, listMode)
        }.flow
    }

    override suspend fun insertUpdateCredential(credential: CredentialDto): CredentialDto {
        val credentialId = roomDao.insertUpdateCredential(credential)
        return roomDao.getCredentialViaId(credentialId)
    }

    override suspend fun deleteCredential(credentialId: Long): CredentialDto {
        val credentialDto = roomDao.getCredentialViaId(credentialId)
        roomDao.deleteCredential(credentialId)
        return credentialDto
    }

    override suspend fun markUnMarkFavourite(credentialId: Long, fav: Boolean): CredentialDto {
        roomDao.markUnMarkFavourite(credentialId, fav)
        return roomDao.getCredentialViaId(credentialId)
    }
}