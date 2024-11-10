package com.example.composegitapp

import com.example.composegitapp.clean_arch_comp.data.data_source.RepoRemoteDataSource
import com.example.composegitapp.clean_arch_comp.data.dto.RepoDto
import com.example.composegitapp.network.IGitHubApi
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlin.test.assertFailsWith
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response
import kotlinx.coroutines.Dispatchers
import okhttp3.ResponseBody

@OptIn(ExperimentalCoroutinesApi::class)
class RepoRemoteDataSourceTest {

    @Mock
    private lateinit var mockApi: IGitHubApi

    private lateinit var dataSource: RepoRemoteDataSource
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        dataSource = RepoRemoteDataSource(
            dispatcher = testDispatcher,
            api = mockApi
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun searchRepositoriesReturnsItemsOnSuccessfulResponse() = runTest {
        val mockResponse = Response.success(RepoDto(items = listOf(RepoDto.RepoItemDto(id = 1, name = "TestRepo"))))
        Mockito.`when`(mockApi.searchRepository("test")).thenReturn(mockResponse)

        val result = dataSource.searchRepositories("test")

        assertEquals(1, result.size)
        assertEquals("TestRepo", result[0].name)
    }

    @Test
    fun searchRepositoriesThrowsValidationExceptionOnUnprocessableEntityResponse() = runTest {
        val mockResponse = Response.error<RepoDto>(422, ResponseBody.create(null, ""))
        Mockito.`when`(mockApi.searchRepository("test")).thenReturn(mockResponse)

        val exception = assertFailsWith<IllegalArgumentException> {
            dataSource.searchRepositories("test")
        }

        assertEquals("Validation failed for the query: test", exception.message)
    }

    @Test
    fun searchRepositoriesThrowsServiceUnavailableExceptionOnServiceUnavailableResponse() = runTest {
        val mockResponse = Response.error<RepoDto>(503, ResponseBody.create(null, ""))
        Mockito.`when`(mockApi.searchRepository("test")).thenReturn(mockResponse)

        val exception = assertFailsWith<IllegalStateException> {
            dataSource.searchRepositories("test")
        }

        assertEquals("Service is unavailable. Please try again later.", exception.message)
    }

    @Test
    fun searchRepositoriesReturnsEmptyListOnFailedResponse() = runTest {
        val mockResponse = Response.error<RepoDto>(404, ResponseBody.create(null, ""))
        Mockito.`when`(mockApi.searchRepository("test")).thenReturn(mockResponse)

        val result = dataSource.searchRepositories("test")

        assertEquals(0, result.size)
    }
}
