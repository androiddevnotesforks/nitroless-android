package com.paraskcd.nitroless.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "fav_tbl",
)
data class FavouriteEmotesTable(
    @PrimaryKey
        val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "repo_url")
        val repoURL: String,

    @ColumnInfo(name = "emote_url")
        val emoteURL: String
)
