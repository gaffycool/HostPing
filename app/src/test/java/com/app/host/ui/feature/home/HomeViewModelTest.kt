package com.app.host.ui.feature.home

import com.app.commondomain.interactors.FetchHostsInteractor
import com.app.commondomain.interactors.PingHostsInteractor
import com.app.commondomain.model.HostModel
import com.app.host.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var fetchHostsInteractor: FetchHostsInteractor

    @MockK
    private lateinit var pingHostsInteractor: PingHostsInteractor

    @Before
    fun setUp() {
        viewModel = HomeViewModel(fetchHostsInteractor, pingHostsInteractor)
    }

    /***
     * WHEN i invoke onPageChange
     * THEN i expect viewstate should updated with same
     */
    @Test
    fun testFetchHosts() = runTest {
        //given
        coEvery { fetchHostsInteractor.get() } returns listOf(
            HostModel("icon", "name", "url")
        )
        //when
        viewModel.fetchHosts()
        //then
        assertEquals(
            listOf(HostUIModel("icon", "name", "url",  PingStatus.InProcess)),
            viewModel.uiState.value.hosts
        )
    }
}