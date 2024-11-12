package com.example.composegitapp

import com.example.composegitapp.clean_arch_comp.data.data_source.RepoRemoteDataSource
import com.example.composegitapp.clean_arch_comp.data.dto.RepoDto
import com.example.composegitapp.network.IGitHubApi
import com.example.composegitapp.network.NetworkParams
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response
import kotlin.test.assertFailsWith

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

        val result = dataSource.searchRepositories(
            query = "test",
            page = 1,
            sort = NetworkParams.Sort.UPDATED,
            order = NetworkParams.Order.DESC,
            perPage = NetworkParams.PER_PAGE_DEFAULT_VALUE
        )

        assertEquals(1, result.size)
        assertEquals("TestRepo", result[0].name)
    }

    @Test
    fun searchRepositoriesThrowsValidationExceptionOnUnprocessableEntityResponse() = runTest {
        val mockResponse = Response.error<RepoDto>(422, "".toResponseBody(null))
        Mockito.`when`(mockApi.searchRepository("test")).thenReturn(mockResponse)

        val exception = assertFailsWith<IllegalArgumentException> {
            dataSource.searchRepositories(
                query = "test",
                page = 1,
                sort = NetworkParams.Sort.UPDATED,
                order = NetworkParams.Order.DESC,
                perPage = NetworkParams.PER_PAGE_DEFAULT_VALUE
            )
        }

        assertEquals("Validation failed for the query: test", exception.message)
    }

    @Test
    fun searchRepositoriesThrowsServiceUnavailableExceptionOnServiceUnavailableResponse() = runTest {
        val mockResponse = Response.error<RepoDto>(503, "".toResponseBody(null))
        Mockito.`when`(
            mockApi.searchRepository(
                query = "test",
                page = 1,
                sort = NetworkParams.Sort.UPDATED,
                order = NetworkParams.Order.DESC,
                perPage = NetworkParams.PER_PAGE_DEFAULT_VALUE
            )
        ).thenReturn(mockResponse)

        val exception = assertFailsWith<IllegalStateException> {
            dataSource.searchRepositories(
                query = "test",
                page = 1,
                sort = NetworkParams.Sort.UPDATED,
                order = NetworkParams.Order.DESC,
                perPage = NetworkParams.PER_PAGE_DEFAULT_VALUE
            )
        }

        assertEquals("Service is unavailable. Please try again later.", exception.message)
    }

    @Test
    fun searchRepositoriesReturnsEmptyListOnFailedResponse() = runTest {
        val mockResponse = Response.error<RepoDto>(404, "".toResponseBody(null))
        Mockito.`when`(mockApi.searchRepository("test")).thenReturn(mockResponse)

        val result = dataSource.searchRepositories(
            query = "test",
            page = 1,
            sort = NetworkParams.Sort.UPDATED,
            order = NetworkParams.Order.DESC,
            perPage = NetworkParams.PER_PAGE_DEFAULT_VALUE
        )

        assertEquals(0, result.size)
    }
}
