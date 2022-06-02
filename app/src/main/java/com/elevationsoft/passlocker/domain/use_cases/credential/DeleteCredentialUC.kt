package com.elevationsoft.passlocker.domain.use_cases.credential

import com.elevationsoft.passlocker.domain.repository.CredentialRepo
import com.elevationsoft.passlocker.utils.common_classes.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteCredentialUC @Inject constructor(private val credentialRepo: CredentialRepo) {
    operator fun invoke(credentialId: Long): Flow<DataState<Boolean>> {
        return flow {
            emit(DataState.Loading(true))
            credentialRepo.deleteCredential(credentialId)
            emit(DataState.Success(true))
            emit(DataState.Loading(false))
        }
    }
}