package com.paraskcd.nitroless.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "repo_tbl")
data class RepoTable(
    @PrimaryKey
        val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "repo_url")
        val repoURL: String
)
