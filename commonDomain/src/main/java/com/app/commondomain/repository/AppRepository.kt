package com.app.commondomain.repository

import com.app.commondomain.model.HostModel

/**
 * interface used to handle all api functions or database operations
 */
interface AppRepository {
    suspend fun fetchHosts(): List<HostModel>
    suspend fun pingHost(url: String): Boolean
}
