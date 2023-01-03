package com.paraskcd.nitroless.repository

import android.util.Log
import com.paraskcd.nitroless.data.DataOrException
import com.paraskcd.nitroless.data.RepoDatabaseDao
import com.paraskcd.nitroless.model.*
import com.paraskcd.nitroless.network.ReposApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RepoRepository @Inject constructor(private val repoDatabaseDao: RepoDatabaseDao) {
    suspend fun getRepoData( dataOrException: DataOrException<Repo, Boolean, Exception>, api: ReposApi ): DataOrException<Repo, Boolean, java.lang.Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getRepoData()

            if (dataOrException.data.toString().isNotEmpty()) {
                dataOrException.loading = false
            }
        } catch (ex: Exception) {
            dataOrException.e = ex
            Log.d("DEBUGGG Exception", ex.message.toString())
        }

        return dataOrException
    }
    suspend fun addRepo(repo: RepoTable) = repoDatabaseDao.addRepo(repo)
    suspend fun deleteRepo(repo: RepoTable) = repoDatabaseDao.deleteRepo(repo)
    fun getAllRepos(): Flow<List<RepoTable>> = repoDatabaseDao.getAllRepos().flowOn(Dispatchers.IO).conflate()
    suspend fun addFrequentlyUsedEmote(emote: FrequentlyUsedEmotesTable) = repoDatabaseDao.addFrequentlyUsedEmotes(emote)
    suspend fun addFrequentlyUsedSticker(sticker: FrequentlyUsedStickersTable) = repoDatabaseDao.addFrequentlyUsedStickers(sticker)
    suspend fun deleteFrequentlyUsedEmote(emote: FrequentlyUsedEmotesTable) = repoDatabaseDao.deleteFrequentlyUsedEmote(emote)
    suspend fun deleteFrequentlyUsedSticker(sticker: FrequentlyUsedStickersTable) = repoDatabaseDao.deleteFrequentlyUsedSticker(sticker)
    fun getAllFrequentlyUsedEmotes(): Flow<List<FrequentlyUsedEmotesTable>> = repoDatabaseDao.getAllFrequentlyUsedEmotes().flowOn(Dispatchers.IO).conflate()
    fun getAllFrequentlyUsedStickers(): Flow<List<FrequentlyUsedStickersTable>> = repoDatabaseDao.getAllFrequentlyUsedStickers().flowOn(Dispatchers.IO).conflate()
    suspend fun addFavouriteEmote(emote: FavouriteEmotesTable) = repoDatabaseDao.addFavouriteEmote(emote)
    suspend fun addFavouriteSticker(sticker: FavouriteStickersTable) = repoDatabaseDao.addFavouriteSticker(sticker)
    suspend fun deleteFavouriteEmote(emote: FavouriteEmotesTable) = repoDatabaseDao.deleteFavouriteEmote(emote)
    suspend fun deleteFavouriteSticker(sticker: FavouriteStickersTable) = repoDatabaseDao.deleteFavouriteSticker(sticker)
    fun getAllFavouriteEmotes(): Flow<List<FavouriteEmotesTable>> = repoDatabaseDao.getAllFavouriteEmotes().flowOn(Dispatchers.IO).conflate()
    fun getAllFavouriteStickers(): Flow<List<FavouriteStickersTable>> = repoDatabaseDao.getAllFavouriteStickers().flowOn(Dispatchers.IO).conflate()
    fun getAllFavouriteEmotesForRepo(repoURL: String): Flow<List<FavouriteEmotesTable>> = repoDatabaseDao.getFavouriteEmotesForRepo(repoURL).flowOn(Dispatchers.IO).conflate()
    fun getAllFavouriteStickersForRepo(repoURL: String): Flow<List<FavouriteStickersTable>> = repoDatabaseDao.getFavouriteStickersForRepo(repoURL).flowOn(Dispatchers.IO).conflate()
}