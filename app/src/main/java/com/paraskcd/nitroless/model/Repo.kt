package com.paraskcd.nitroless.model

import java.util.UUID

data class Repo(
    var id: UUID?,
    var selected: Boolean = false,
    var url: String?,
    val author: String?,
    val description: String?,
    val emotes: List<Emote>,
    val icon: String,
    val keywords: String?,
    val name: String,
    val path: String,
    var favouriteEmotes: List<FavouriteEmotesTable>?
)