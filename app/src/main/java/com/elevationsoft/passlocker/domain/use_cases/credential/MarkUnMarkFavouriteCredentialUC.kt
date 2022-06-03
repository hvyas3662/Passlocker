package com.elevationsoft.passlocker.domain.use_cases.credential

import com.elevationsoft.passlocker.data.repository.CredentialRepoImpl
import com.elevationsoft.passlocker.domain.models.Credential
import com.elevationsoft.passlocker.utils.common_classes.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MarkUnMarkFavouriteCredentialUC @Inject constructor(private val credentialRepo: CredentialRepoImpl) {
    operator fun invoke(credentialId: Long, fav: Boolean): Flow<DataState<Credential>> {
        return flow {
            emit(DataState.Loading(true))
            val credentialDto = credentialRepo.markUnMarkFavourite(credentialId, fav)
            emit(DataState.Success(credentialDto.toCredential()))
        }
    }
}