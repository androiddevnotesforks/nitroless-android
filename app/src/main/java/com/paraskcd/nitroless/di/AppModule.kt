package com.paraskcd.nitroless.di

import android.content.Context
import androidx.room.Room
import com.paraskcd.nitroless.data.RepoDatabase
import com.paraskcd.nitroless.data.RepoDatabaseDao
import com.paraskcd.nitroless.network.CommunityReposApi
import com.paraskcd.nitroless.repository.CommunityReposRepository
import com.paraskcd.nitroless.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
        fun provideRepoDBDao(repoDatabase: RepoDatabase): RepoDatabaseDao = repoDatabase.repoDao()

    @Singleton
    @Provides
        fun provideRepoDB(@ApplicationContext context: Context): RepoDatabase = Room.databaseBuilder(context, RepoDatabase::class.java, "repo_db").fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
        fun providesCommunityReposRepository(api: CommunityReposApi) = CommunityReposRepository(api)

    @Singleton
    @Provides
        fun providesCommunityReposApi(): CommunityReposApi {
            return Retrofit.Builder().baseUrl(Constants.BASE_URL_COMMUNITY_REPOS).addConverterFactory(GsonConverterFactory.create()).build().create(CommunityReposApi::class.java)
        }
}