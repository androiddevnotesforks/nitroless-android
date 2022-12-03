package com.paraskcd.nitroless.di

import android.content.Context
import androidx.room.Room
import com.paraskcd.nitroless.data.RepoDatabase
import com.paraskcd.nitroless.data.RepoDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
}