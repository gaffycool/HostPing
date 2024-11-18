package com.app.commondomain.interactors

import com.app.commondomain.model.HostModel
import com.app.commondomain.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Class is used to return each host as soon as it complete the 5 times Ping
 */
class PingHostsInteractor @Inject constructor(
    private val appRepository: AppRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun get(hosts: List<HostModel>) = hosts
        .asFlow() // Convert the list to a Flow
        .flatMapMerge(concurrency = 5) { host -> // Run up to 5 tasks in parallel
            flow {
                val result = pingHost(host)
                emit(result)
            }
        }
        .flowOn(Dispatchers.IO) // Use IO dispatcher for network operations

    private suspend fun pingHost(host: HostModel): Pair<HostModel, Long> =
        withContext(Dispatchers.IO) {//running network task in the background thread
            val latencyResults = mutableListOf<Long>()
            repeat(5) {//ensures the host pings only 5 times
                try {
                    val startTime = System.nanoTime()
                    val ping = appRepository.pingHost(host.url)
                    if (ping) {
                        val endTime = System.nanoTime()
                        val millis = (endTime - startTime) / 1_000_000 // Convert to milliseconds
                        print("${host.name} $ping $millis")
                        latencyResults.add(millis)
                    }
                } catch (e: Exception) {
                    print("${host.name} ${e.localizedMessage}")
                }
            }
            return@withContext Pair(
                host,
                if (latencyResults.isNotEmpty()) latencyResults.average().toLong() else -1
            )
        }
}
