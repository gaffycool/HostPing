package com.app.commondomain.interactors

import com.app.commondomain.model.HostModel
import com.app.commondomain.repository.AppRepository
import javax.inject.Inject

/**
 * Class is used to return list of [HostModel] from [AppRepository]
 */
class FetchHostsInteractor @Inject constructor(
    private val appRepository: AppRepository
) {
    suspend fun get(): List<HostModel> = appRepository.fetchHosts()
}
