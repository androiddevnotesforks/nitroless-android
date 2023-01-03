package com.paraskcd.nitroless.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paraskcd.nitroless.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDatabaseDao {
    @Query("SELECT * FROM repo_tbl")
        fun getAllRepos(): Flow<List<RepoTable>>

    @Query("SELECT * FROM freqused_tbl")
        fun getAllFrequentlyUsedEmotes(): Flow<List<FrequentlyUsedEmotesTable>>

    @Query("SELECT * FROM freqused_stickers_tbl")
        fun getAllFrequentlyUsedStickers(): Flow<List<FrequentlyUsedStickersTable>>

    @Query("SELECT * FROM fav_tbl")
        fun getAllFavouriteEmotes(): Flow<List<FavouriteEmotesTable>>

    @Query("SELECT * FROM fav_tbl WHERE repo_url= :repoURL")
        fun getFavouriteEmotesForRepo(repoURL: String): Flow<List<FavouriteEmotesTable>>

    @Query("SELECT * FROM fav_stickers_tbl")
        fun getAllFavouriteStickers(): Flow<List<FavouriteStickersTable>>

    @Query("SELECT * FROM fav_stickers_tbl WHERE repo_url= :repoURL")
        fun getFavouriteStickersForRepo(repoURL: String): Flow<List<FavouriteStickersTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun addRepo(repo: RepoTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun addFavouriteEmote(favEmote: FavouriteEmotesTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun addFavouriteSticker(favSticker: FavouriteStickersTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun addFrequentlyUsedEmotes(freqUsed: FrequentlyUsedEmotesTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun addFrequentlyUsedStickers(freqUsed: FrequentlyUsedStickersTable)

    @Delete
        suspend fun deleteRepo(repo: RepoTable)

    @Delete
        suspend fun deleteFavouriteEmote(favEmote: FavouriteEmotesTable)

    @Delete
        suspend fun deleteFavouriteSticker(favSticker: FavouriteStickersTable)

    @Delete
        suspend fun deleteFrequentlyUsedEmote(freqUsed: FrequentlyUsedEmotesTable)

    @Delete
        suspend fun deleteFrequentlyUsedSticker(freqUsed: FrequentlyUsedStickersTable)
}