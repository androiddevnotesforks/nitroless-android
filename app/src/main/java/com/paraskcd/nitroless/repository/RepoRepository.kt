package com.paraskcd.nitroless.repository

import com.paraskcd.nitroless.data.RepoDatabaseDao
import com.paraskcd.nitroless.model.FavouriteEmotesTable
import com.paraskcd.nitroless.model.FrequentlyUsedEmotesTable
import com.paraskcd.nitroless.model.RepoTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RepoRepository @Inject constructor(private val repoDatabaseDao: RepoDatabaseDao) {
    suspend fun addRepo(repo: RepoTable) = repoDatabaseDao.addRepo(repo)
    suspend fun deleteRepo(repo: RepoTable) = repoDatabaseDao.deleteRepo(repo)
    suspend fun getAllRepos(): Flow<List<RepoTable>> = repoDatabaseDao.getAllRepos().flowOn(Dispatchers.IO).conflate()
    suspend fun addFrequentlyUsedEmote(emote: FrequentlyUsedEmotesTable) = repoDatabaseDao.addFrequentlyUsedEmotes(emote)
    suspend fun deleteFrequentlyUsedEmote(emote: FrequentlyUsedEmotesTable) = repoDatabaseDao.deleteFrequentlyUsedEmote(emote)
    suspend fun getAllFrequentlyUsedEmotes(): Flow<List<FrequentlyUsedEmotesTable>> = repoDatabaseDao.getAllFrequentlyUsedEmotes().flowOn(Dispatchers.IO).conflate()
    suspend fun addFavouriteEmote(emote: FavouriteEmotesTable) = repoDatabaseDao.addFavouriteEmote(emote)
    suspend fun deleteFavouriteEmote(emote: FavouriteEmotesTable) = repoDatabaseDao.deleteFavouriteEmote(emote)
    suspend fun getAllFavouriteEmotes(): Flow<List<FavouriteEmotesTable>> = repoDatabaseDao.getAllFavouriteEmotes().flowOn(Dispatchers.IO).conflate()
    suspend fun getAllFavouriteEmotesForRepo(repoURL: String): Flow<List<FavouriteEmotesTable>> = repoDatabaseDao.getFavouriteEmotesForRepo(repoURL).flowOn(Dispatchers.IO).conflate()
}