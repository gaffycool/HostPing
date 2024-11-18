package com.app.commondata.api

import retrofit2.Response
import retrofit2.http.HEAD

/**
 * This class used to ping define host from retrofit instance
 */
interface PingService {
    @HEAD("/")
    suspend fun ping(): Response<Void>
}
