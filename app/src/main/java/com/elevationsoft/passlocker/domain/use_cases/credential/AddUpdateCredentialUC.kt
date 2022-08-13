package com.elevationsoft.passlocker.domain.use_cases.credential

import com.elevationsoft.passlocker.data.dto.CredentialDto.Companion.toCredentialDto
import com.elevationsoft.passlocker.domain.models.Credential
import com.elevationsoft.passlocker.domain.repository.CredentialRepo
import com.elevationsoft.passlocker.utils.common_classes.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddUpdateCredentialUC @Inject constructor(private val credentialRepo: CredentialRepo) {

    operator fun invoke(credential: Credential): Flow<DataState<Credential>> {
        return flow {
            emit(DataState.Loading(true))
            val credentialDto = credentialRepo.insertUpdateCredential(credential.toCredentialDto())
            emit(DataState.Success(credentialDto.toCredential()))
        }
    }
}