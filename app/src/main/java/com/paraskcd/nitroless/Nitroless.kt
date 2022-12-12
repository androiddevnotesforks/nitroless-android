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
class Nitroless: Application() {}