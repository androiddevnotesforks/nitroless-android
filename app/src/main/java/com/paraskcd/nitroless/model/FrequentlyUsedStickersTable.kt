package com.paraskcd.nitroless.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "freqused_stickers_tbl"
)
data class FrequentlyUsedStickersTable(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "sticker_url")
    val stickerURL: String
)
