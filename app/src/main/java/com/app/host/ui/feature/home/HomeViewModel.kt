package com.app.host.ui.feature.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import com.app.commondomain.interactors.FetchHostsInteractor
import com.app.commondomain.interactors.PingHostsInteractor
import com.app.commondomain.model.HostModel
import com.app.host.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * HomeViewModel
 */
@HiltViewModel
class HomeViewModel @Inject internal constructor(
    private val fetchHostsInteractor: FetchHostsInteractor,
    private val pingHostsInteractor: PingHostsInteractor,
) : BaseViewModel<HomeViewState>() {

    override fun defaultState(): HomeViewState = HomeViewState()

    init {
        fetchHosts()
    }

    /**
     * Functions loads lists of hosts from API via FetchHostsInteractor
     * and run task to check each host latency
     */
    @VisibleForTesting
    fun fetchHosts() {
        viewModelScope.launch {
            val hosts = fetchHostsInteractor.get()
            newState { it.copy(hosts = hosts.map(::HostUIModel)) }
            async {
                delay(1500)
                newState { it.copy(isRefreshing = false) }
            }
            checkHostLatency(hosts)
        }
    }

    /**
     * Function called when user pull to refresh the list
     * It make isRefreshing true to show loading indicator on screen and re fetch list of hosts
     */
    fun onRefresh() {
        viewModelScope.launch {
            newState { it.copy(isRefreshing = true) }
            fetchHosts()
        }
    }

    /**
     * Functions called when user click on individual host to re check its latency
     */
    fun retryLatencyCheckClick(hostUIModel: HostUIModel) {
        viewModelScope.launch {
            updateHostState(hostUIModel.url) { host ->
                host.copy(pingStatus = PingStatus.InProcess)
            }
            checkHostLatency(listOf(hostUIModel.toDomainModel()))
        }
    }

    /**
     * Functions is reusable to check latency ping status for each host and update individual
     * host when pingHostsInteractor return each host with its average latency
     */
    private suspend fun checkHostLatency(hosts: List<HostModel>) {
        pingHostsInteractor.get(hosts).collectLatest { pair ->
            updateHostState(pair.first.url) {
                HostUIModel(pair.first, pair.second)
            }
        }
    }

    /**
     * Reusable function to update a host in the list based on its URL.
     */
    private fun updateHostState(url: String, update: (HostUIModel) -> HostUIModel) {
        newState { currentState ->
            val index = currentState.hosts.indexOfFirst { it.url == url }
            if (index != -1) {
                currentState.copy(
                    hosts = currentState.hosts.toMutableList().apply {
                        set(index, update(currentState.hosts[index]))
                    }
                )
            } else {
                currentState
            }
        }
    }

    /**
     * Function called when user clicks on sort button
     */
    fun sortClick() {
        viewModelScope.launch {
            newState {
                it.copy(
                    hosts = it.hosts.sortByLatency(it.isSorted),
                    isSorted = !it.isSorted,
                    showSnackBarMessage = true
                )
            }
            delay(3000)
            newState { it.copy(showSnackBarMessage = false) }
        }
    }

    /**
     * sortByLatency extension used to sort list of hosts by latency value in PingStatus.Done
     */
    private fun List<HostUIModel>.sortByLatency(isAscending: Boolean): List<HostUIModel> {
        return this.sortedWith(
            compareBy<HostUIModel> { host ->
                when (host.pingStatus) {
                    is PingStatus.Done -> 0   // Highest priority for Done
                    is PingStatus.InProcess -> 1
                    is PingStatus.Failed -> 2
                }
            }.thenBy { host ->
                // Sort by latency if status is Done, otherwise use a default large value
                val latency = (host.pingStatus as? PingStatus.Done)?.latency ?: Long.MAX_VALUE
                if (isAscending) latency else -latency // Change sorting order based on isAscending
            }
        )
    }
}

/**
 * HomeViewState can have all variables or data related to Homes screens
 */
data class HomeViewState(
    val hosts: List<HostUIModel> = emptyList(),
    val isRefreshing: Boolean = false,
    val isSorted: Boolean = false,
    val showSnackBarMessage: Boolean = false
)

/**
 * HostUIModel is UI model to display list item on screens
 */
data class HostUIModel(
    val icon: String,
    val name: String,
    val url: String,
    val pingStatus: PingStatus = PingStatus.InProcess
) {
    constructor(hostModel: HostModel, latency: Long = 0) : this(
        icon = hostModel.icon,
        name = hostModel.name,
        url = hostModel.url,
        pingStatus = when (latency) {
            0L -> PingStatus.InProcess
            -1L -> PingStatus.Failed
            else -> PingStatus.Done(latency)
        }
    )

    fun toDomainModel() = HostModel(icon, name, url)
}

/**
 * PingStatus sealed class used to show host ping status on Host item view
 */
sealed class PingStatus {
    data object Failed : PingStatus()
    data object InProcess : PingStatus()
    data class Done(val latency: Long) : PingStatus()
}
