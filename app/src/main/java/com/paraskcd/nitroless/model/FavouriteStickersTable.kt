package com.paraskcd.nitroless.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "fav_stickers_tbl"
)
data class FavouriteStickersTable(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "repo_url")
    val repoURL: String,

    @ColumnInfo(name = "sticker_url")
    val stickerURL: String
)