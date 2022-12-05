package com.paraskcd.nitroless.network

import com.paraskcd.nitroless.model.Repo
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface ReposApi {
    @GET("index.json")
    suspend fun getRepoData(): Repo
}