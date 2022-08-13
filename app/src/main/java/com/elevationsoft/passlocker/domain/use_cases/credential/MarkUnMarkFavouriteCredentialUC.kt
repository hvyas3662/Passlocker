package com.elevationsoft.passlocker.domain.use_cases.credential

import com.elevationsoft.passlocker.domain.models.Credential
import com.elevationsoft.passlocker.domain.repository.CredentialRepo
import com.elevationsoft.passlocker.utils.common_classes.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MarkUnMarkFavouriteCredentialUC @Inject constructor(private val credentialRepo: CredentialRepo) {
    operator fun invoke(credentialId: Long, fav: Boolean): Flow<DataState<Credential>> {
        return flow {
            emit(DataState.Loading(true))
            val credentialDto = credentialRepo.markUnMarkFavourite(credentialId, fav)
            emit(DataState.Success(credentialDto.toCredential()))
        }
    }
}