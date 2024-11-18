package com.app.commondomain.interactors

import com.app.commondomain.model.HostModel
import com.app.commondomain.repository.AppRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FetchHostsInteractorTest {

    private val appRepository: AppRepository = mockk()
    private lateinit var fetchHostsInteractor: FetchHostsInteractor

    @Before
    fun setUp() {
        fetchHostsInteractor = FetchHostsInteractor(appRepository)
    }

    /***
     * GIVEN appRepository.fetchHosts
     * WHEN i invoke interactor
     * THEN i expect list of HostModel to be returned
     */
    @Test
    fun testFetchHosts() = runTest {
        val list: List<HostModel> = mockk()
        //given
        coEvery { appRepository.fetchHosts() } returns list

        //when
        val result = fetchHostsInteractor.get()

        //then
        coVerify { appRepository.fetchHosts() }
        assertEquals(list, result)
    }
}