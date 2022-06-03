package com.elevationsoft.passlocker.domain.repository

import androidx.paging.PagingData
import com.elevationsoft.passlocker.data.dto.CredentialDto
import com.elevationsoft.passlocker.domain.models.Credential
import com.elevationsoft.passlocker.domain.utils.CredentialListMode
import kotlinx.coroutines.flow.Flow

interface CredentialRepo {
    fun getCredentialPage(
        listMode: CredentialListMode
    ): Flow<PagingData<CredentialDto>>

    suspend fun insertUpdateCredential(credential: CredentialDto): CredentialDto

    suspend fun deleteCredential(credentialId: Long): CredentialDto

    suspend fun markUnMarkFavourite(credentialId: Long, fav: Boolean): CredentialDto
}