package com.paraskcd.nitroless.model

data class Repo(
    var selected: Boolean = false,
    val url: String,
    val author: String?,
    val description: String?,
    val emotes: List<Emote>,
    val icon: String,
    val keywords: String?,
    val name: String,
    val path: String
)