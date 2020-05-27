package com.hackernews.models

data class User(
    val about: String,
    val created: Int,
    val delay: Int,
    val id: String,
    val karma: Int,
    val submitted: List<Int>
)