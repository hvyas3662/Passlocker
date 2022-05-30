package com.elevationsoft.passlocker.domain.use_cases.credential

import android.net.wifi.hotspot2.pps.Credential
import com.elevationsoft.passlocker.data.repository.CredentialRepoImpl
import com.elevationsoft.passlocker.domain.utils.CredentialListMode
import com.elevationsoft.passlocker.utils.common_classes.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCredentialListUC @Inject constructor(private val credentialRepo: CredentialRepoImpl) {

    operator fun invoke(listMode: CredentialListMode): Flow<DataState<List<Credential>>> {
        return flow {

            when (listMode) {
                is CredentialListMode.Favourite -> {

                }
                is CredentialListMode.FavouriteSearch -> {

                }

                is CredentialListMode.Category -> {

                }

                is CredentialListMode.CategorySearch -> {

                }

            }

        }
    }
}