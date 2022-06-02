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