package com.app.host.data.repository

import com.app.commondata.api.ApiService
import com.app.commondata.dto.HostItemDTO
import com.app.commondata.dto.HostListResponseDTO
import com.app.commondata.repository.AppRepositoryImpl
import com.app.commondomain.model.HostModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import retrofit2.Response

@ExperimentalCoroutinesApi
class AppRepositoryImplTest {

    private val apiService: ApiService = mockk()
    private lateinit var repository: AppRepositoryImpl

    @Before
    fun setUp() {
        repository = AppRepositoryImpl(apiService)
    }

    /***
     * GIVEN apiService.fetchHosts returns success response
     * WHEN i invoke fetchHosts
     * THEN i expect list of HostModel to be returned
     */
    @Test
    fun testFetchHosts() = runTest {
        //given
        val responseDTO = HostListResponseDTO()
        responseDTO.add(HostItemDTO("icon", "name", "url"))

        val response = Response.success(responseDTO)
        coEvery { apiService.fetchHosts() } returns response

        //when
        val result = repository.fetchHosts()

        //then
        assertEquals(listOf(HostModel("icon", "name", "url")), result)
    }

    /***
     * GIVEN apiService.fetchHosts returns success response
     * WHEN i invoke fetchHosts
     * THEN i expect list of HostModel to be returned
     */
    @Test
    fun `testFetchHosts when api return error then check it throw IllegalStateException`() = runTest {
        //given
        val responseDTO = HostListResponseDTO()
        responseDTO.add(HostItemDTO("icon", "name", "url"))

        val errorBody = "{}".toResponseBody("application/json".toMediaTypeOrNull())
        val response = Response.error<HostListResponseDTO>(400, errorBody)
        coEvery { apiService.fetchHosts() } returns response
        //when
        assertThrows<IllegalStateException> {
            repository.fetchHosts()
        }
    }
}
