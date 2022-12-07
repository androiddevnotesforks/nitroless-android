package com.paraskcd.nitroless

import android.app.Application
import androidx.room.Room
import com.paraskcd.nitroless.data.RepoDatabase
import com.paraskcd.nitroless.network.CommunityReposApi
import com.paraskcd.nitroless.repository.CommunityReposRepository
import com.paraskcd.nitroless.repository.RepoRepository
import com.paraskcd.nitroless.utils.Constants
import dagger.hilt.android.HiltAndroidApp
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@HiltAndroidApp
class Nitroless: Application() {
    val database by lazy { Room.databaseBuilder(this, RepoDatabase::class.java, "repo_db").fallbackToDestructiveMigration().build() }
    val repository by lazy { RepoRepository(database.repoDao()) }
    val communityReposApi by lazy { Retrofit.Builder().baseUrl(Constants.BASE_URL_COMMUNITY_REPOS).addConverterFactory(GsonConverterFactory.create()).build().create(CommunityReposApi::class.java) }
    val communityReposRepository by lazy { CommunityReposRepository(communityReposApi) }
}