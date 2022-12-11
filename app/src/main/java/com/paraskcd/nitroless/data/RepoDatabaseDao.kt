package com.paraskcd.nitroless.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paraskcd.nitroless.model.FavouriteEmotesTable
import com.paraskcd.nitroless.model.FrequentlyUsedEmotesTable
import com.paraskcd.nitroless.model.RepoTable
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDatabaseDao {
    @Query("SELECT * FROM repo_tbl")
        fun getAllRepos(): Flow<List<RepoTable>>

    @Query("SELECT * FROM freqused_tbl")
        fun getAllFrequentlyUsedEmotes(): Flow<List<FrequentlyUsedEmotesTable>>

    @Query("SELECT * FROM fav_tbl")
        fun getAllFavouriteEmotes(): Flow<List<FavouriteEmotesTable>>

    @Query("SELECT * FROM fav_tbl WHERE repo_url= :repoURL")
        fun getFavouriteEmotesForRepo(repoURL: String): Flow<List<FavouriteEmotesTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun addRepo(repo: RepoTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun addFavouriteEmote(favEmote: FavouriteEmotesTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun addFrequentlyUsedEmotes(freqUsed: FrequentlyUsedEmotesTable)

    @Delete
        suspend fun deleteRepo(repo: RepoTable)

    @Delete
        suspend fun deleteFavouriteEmote(favEmote: FavouriteEmotesTable)

    @Delete
        suspend fun deleteFrequentlyUsedEmote(freqUsed: FrequentlyUsedEmotesTable)
}