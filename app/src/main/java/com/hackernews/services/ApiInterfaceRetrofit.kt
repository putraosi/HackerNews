package com.hackernews.services

import com.hackernews.models.Comment
import com.hackernews.models.Story
import com.hackernews.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by Osi Novandama Putra on 27/05/20.
 */

interface ApiInterfaceRetrofit {
    @GET("topstories.json?print=pretty")
    fun getTopStory(): Call<List<Int>>

    @GET
    fun getSrory(@Url url: String): Call<Story>

    @GET
    fun getUser(@Url url: String): Call<User>

    @GET
    fun getComment(@Url url: String): Call<Comment>


}