package com.paraskcd.nitroless.repository

import android.util.Log
import com.paraskcd.nitroless.data.DataOrException
import com.paraskcd.nitroless.model.CommunityRepos
import com.paraskcd.nitroless.network.CommunityReposApi
import javax.inject.Inject

class CommunityReposRepository @Inject constructor(private val api: CommunityReposApi) {
    private val dataOrException = DataOrException<CommunityRepos, Boolean, Exception>()
    suspend fun getCommunityRepos(): DataOrException<CommunityRepos, Boolean, java.lang.Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getCommunityRepos()

            if (dataOrException.data.toString().isNotEmpty()) {
                dataOrException.loading = false
            }
        } catch (ex: Exception) {
            dataOrException.e = ex
        }

        return dataOrException
    }
}