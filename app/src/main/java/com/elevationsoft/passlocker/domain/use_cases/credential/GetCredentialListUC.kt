package com.elevationsoft.passlocker.domain.use_cases.credential

import androidx.paging.PagingData
import androidx.paging.map
import com.elevationsoft.passlocker.domain.models.Credential
import com.elevationsoft.passlocker.domain.repository.CredentialRepo
import com.elevationsoft.passlocker.domain.utils.CredentialListMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCredentialListUC @Inject constructor(private val credentialRepo: CredentialRepo) {
    operator fun invoke(listMode: CredentialListMode): Flow<PagingData<Credential>> {
        return credentialRepo.getCredentialPage(listMode).map {
            it.map { credentialDto ->
                credentialDto.toCredential()
            }
        }
    }
}