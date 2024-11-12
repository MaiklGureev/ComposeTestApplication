package com.example.composegitapp.network

import com.example.composegitapp.clean_arch_comp.data.dto.RepoBranchesDto
import com.example.composegitapp.clean_arch_comp.data.dto.RepoDto
import com.example.composegitapp.clean_arch_comp.data.dto.RepoListDto
import com.example.composegitapp.clean_arch_comp.data.dto.UserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//https://docs.github.com/en/rest/search/search?apiVersion=2022-11-28
interface IGitHubApi {

    /**
     * Find repositories via various criteria. This method returns up to 100 results per page.
     * @param query The query contains one or more search keywords and qualifiers.
     * @param sort Sorts the results of your query by number of stars, forks, or help-wanted-issues or how recently the items were updated
     * @param order Determines whether the first search result returned is the highest number of matches (desc) or lowest number of matches (asc). This parameter is ignored unless you provide sort.
     * @param perPage The number of results per page (max 100)
     * @param page The page number of the results to fetch
     *
     * HTTP response status codes for "Search repositories"
     * Status code	Description
     *
     * 200
     * OK
     *
     * 304
     * Not modified
     *
     * 422
     * Validation failed, or the endpoint has been spammed.
     *
     * 503
     * Service unavailable
     */
    @GET("/search/repositories")
    suspend fun searchRepository(
        @Query("q") query: String,
        @Query("sort") sort: String = NetworkParams.Sort.UPDATED,
        @Query("order") order: String = NetworkParams.Order.DESC,
        @Query("per_page") perPage: Int = NetworkParams.PER_PAGE_DEFAULT_VALUE,
        @Query("page") page: Int = 1,
    ): Response<RepoDto>

    /**
     * Find users via various criteria. This method returns up to 100 results per page.
     * @param query The query contains one or more search keywords and qualifiers.
     * @param sort Sorts the results of your query by number of stars, forks, or help-wanted-issues or how recently the items were updated
     * @param order Determines whether the first search result returned is the highest number of matches (desc) or lowest number of matches (asc). This parameter is ignored unless you provide sort.
     * @param perPage The number of results per page (max 100)
     * @param page The page number of the results to fetch
     *
     * HTTP response status codes for "Search repositories"
     * Status code	Description
     *
     * 200
     * OK
     *
     * 304
     * Not modified
     *
     * 422
     * Validation failed, or the endpoint has been spammed.
     *
     * 503
     * Service unavailable
     */
    @GET("/search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("sort") sort: String = NetworkParams.Sort.UPDATED,
        @Query("order") order: String = NetworkParams.Order.DESC,
        @Query("per_page") perPage: Int = NetworkParams.PER_PAGE_DEFAULT_VALUE,
        @Query("page") page: Int = 1,
    ): Response<UserDto>

    /**
     * Find user repos.
     * @param userName user name
     */
    @GET("/users/{userName}/repos")
    suspend fun getUserRepos(
        @Path("userName") userName: String,
    ): Response<RepoListDto>

    /**
     * Find user branches in repo.
     * @param userName user name
     * @param repoName user repo
     */
    //https://docs.github.com/en/rest/repos/contents?apiVersion=2022-11-28#download-a-repository-archive-zip
    @GET("/repos/{userName}/{repoName}/branches")
    suspend fun getUserRepoBranches(
        @Path("userName") userName: String,
        @Path("repoName") repoName: String,
    ): Response<RepoBranchesDto>

}
