package com.app.commondata.repository

import com.app.commondata.api.ApiService
import com.app.commondata.api.PingService
import com.app.commondata.dto.HostItemDTO
import com.app.commondomain.model.HostModel
import com.app.commondomain.repository.AppRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.net.HttpURLConnection
import javax.inject.Inject

/**
 * Repository module for handling data operations and calling api related to [ApiService] class
 */
class AppRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : AppRepository {

    /**
     * Function returns list [HostModel] [apiService.fetchHosts] return HTTP_OK response code
     * and throw IllegalStateException when api failed
     */
    override suspend fun fetchHosts(): List<HostModel> {
        val response = apiService.fetchHosts()
        when (response.code()) {
            HttpURLConnection.HTTP_OK -> {
                return response.body()?.map(HostItemDTO::transform) ?: emptyList()
            }

            else -> throw IllegalStateException("Failed to load, Please try again")
        }
    }

    /**
     * Function returns list true [pingService.ping] return HTTP_OK response code
     * and throw IllegalStateException when api failed
     */
    override suspend fun pingHost(url: String): Boolean {
        val pingService = createRetrofitClient("https://$url")
        return pingService.ping().code() == HttpURLConnection.HTTP_OK
    }

    /**
     * Function used to return PingService with new retrofit instance using given baseUrl
     * @param baseUrl pass host url to create retrofit instance
     */
    private fun createRetrofitClient(baseUrl: String): PingService {
        val okHttpClient = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
        return retrofit.create(PingService::class.java)
    }
}
