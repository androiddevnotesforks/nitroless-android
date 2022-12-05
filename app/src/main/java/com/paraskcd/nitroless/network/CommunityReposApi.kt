package com.paraskcd.nitroless.network

import com.paraskcd.nitroless.model.CommunityRepos
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface CommunityReposApi {
    @GET("default.json")
    suspend fun getCommunityRepos(): CommunityRepos
}