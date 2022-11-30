package com.paraskcd.nitroless.data

data class Repo(
    var selected: Boolean = false,
    val url: String,
    val name: String,
    val path: String,
    val author: String?,
    val emote: Emote
)
