package com.elevationsoft.passlocker.data.paging_data_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.elevationsoft.passlocker.data.dto.CredentialDto
import com.elevationsoft.passlocker.data.local.room.RoomDao
import com.elevationsoft.passlocker.domain.utils.CredentialListMode
import timber.log.Timber

class CredentialDataSource constructor(
    private val roomDao: RoomDao,
    private val listMode: CredentialListMode
) : PagingSource<Int, CredentialDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CredentialDto> {
        val page: Int = params.key ?: 1
        val rowCount = 10
        val startIndex: Int = (page - 1) * rowCount

        Timber.tag("CREDENTIAL_PAGE_LOADING").d("${listMode::class.simpleName} of page:$page")
        var dataList: List<CredentialDto> = mutableListOf()
        when (listMode) {
            is CredentialListMode.Favourite -> {
                dataList = roomDao.getFavCredentialPage(startIndex, rowCount)
            }
            is CredentialListMode.FavouriteSearch -> {
                dataList = roomDao.getFavCredentialPage(listMode.searchStr, startIndex, rowCount)
            }
            is CredentialListMode.Category -> {
                dataList = roomDao.getCredentialPage(listMode.categoryId, startIndex, rowCount)
            }
            is CredentialListMode.CategorySearch -> {
                dataList = roomDao.getCredentialPage(
                    listMode.searchStr,
                    listMode.categoryId,
                    startIndex,
                    rowCount
                )
            }
        }

        return if (page > 1 || dataList.isNotEmpty()) {
            LoadResult.Page(
                data = dataList,
                prevKey = null,
                nextKey = if (dataList.isNotEmpty()) {
                    (page + 1)
                } else {
                    null
                }
            )
        } else {
            LoadResult.Error(Throwable("No data found"))
        }

    }

    override fun getRefreshKey(state: PagingState<Int, CredentialDto>): Int? {
        return null
    }


}