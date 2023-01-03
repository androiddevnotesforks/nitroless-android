package com.paraskcd.nitroless.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.paraskcd.nitroless.model.*
import com.paraskcd.nitroless.utils.UUIDConverter

@Database(
    entities = [RepoTable::class, FrequentlyUsedEmotesTable::class, FavouriteEmotesTable::class, FrequentlyUsedStickersTable::class, FavouriteStickersTable::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(UUIDConverter::class)
abstract class RepoDatabase: RoomDatabase() {
    abstract fun repoDao(): RepoDatabaseDao
}