package com.paraskcd.nitroless.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "freqused_tbl",
)
data class FrequentlyUsedEmotesTable(
    @PrimaryKey
        val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "emote_url")
        val emoteURL: String
)
