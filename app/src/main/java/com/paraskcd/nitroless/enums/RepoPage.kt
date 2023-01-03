package com.paraskcd.nitroless.enums

enum class RepoPage(val value: Int) {
    EMOTES(0), STICKERS(1);
    companion object {
        fun getByValue(value: Int) = values().firstOrNull { it.value == value }
    }
}