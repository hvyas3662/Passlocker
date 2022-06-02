package com.elevationsoft.passlocker.domain.use_cases.credential

import com.elevationsoft.passlocker.data.dto.CredentialDto.Companion.toCredentialDto
import com.elevationsoft.passlocker.data.repository.CredentialRepoImpl
import com.elevationsoft.passlocker.domain.models.Credential
import com.elevationsoft.passlocker.utils.common_classes.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddUpdateCredentialUC @Inject constructor(private val credentialRepo: CredentialRepoImpl) {

    operator fun invoke(credential: Credential): Flow<DataState<Boolean>> {
        return flow {
            emit(DataState.Loading(true))
            credentialRepo.insertUpdateCredential(credential.toCredentialDto())
            emit(DataState.Success(true))
        }
    }
}