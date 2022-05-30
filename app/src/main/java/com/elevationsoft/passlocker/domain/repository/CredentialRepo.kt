package com.elevationsoft.passlocker.domain.repository

import com.elevationsoft.passlocker.data.dto.CredentialDto

interface CredentialRepo {
    suspend fun getFavCredentialPage(startIndex: Int, rowCount: Int): List<CredentialDto>

    suspend fun getCredentialPage(
        catId: Long,
        startIndex: Int,
        rowCount: Int
    ): List<CredentialDto>

    suspend fun getCredentialPage(
        search: String,
        catId: Long,
        startIndex: Int,
        rowCount: Int
    ): List<CredentialDto>


    suspend fun insertUpdateCredential(credential: CredentialDto)

    suspend fun deleteCredential(credentialId: Long)
}