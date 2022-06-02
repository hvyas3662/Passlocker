package com.elevationsoft.passlocker.domain.use_cases.credential

import com.elevationsoft.passlocker.data.repository.CredentialRepoImpl
import com.elevationsoft.passlocker.utils.common_classes.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MarkUnMarkFavouriteCredentialUC @Inject constructor(private val credentialRepo: CredentialRepoImpl) {
    operator fun invoke(credentialId: Long, fav: Boolean): Flow<DataState<Boolean>> {
        return flow {
            emit(DataState.Loading(true))
            credentialRepo.markUnMarkFavourite(credentialId, fav)
            emit(DataState.Success(true))
            emit(DataState.Loading(false))
        }
    }
}