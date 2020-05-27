package com.hackernews.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.hackernews.R
import com.hackernews.adapters.AdapterStory
import com.hackernews.models.Story
import com.hackernews.services.APIHelper
import com.hackernews.services.ApiClientRetrofit
import kotlinx.android.synthetic.main.activity_top_stories.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopStories : AppCompatActivity() {

    var listItem: List<Int>? = null
    var listStory = mutableListOf<Story>()
    var min = 0
    var max = 19

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_stories)
        init()
    }

    private fun init() {
        toolbar_text.text = "Top Stories"
        setSupportActionBar(toolbar)

        progressbar.visibility = View.VISIBLE
        recycler_top_story.visibility = View.INVISIBLE
        btn_fav_story.visibility = View.GONE

        getTopStories()
    }

    private fun getTopStories() {
        val call = ApiClientRetrofit.getClient.getTopStory()
        APIHelper.enqueueWithRetry(call, object: Callback<List<Int>>{
            override fun onFailure(call: Call<List<Int>>, t: Throwable) {
            }

            override fun onResponse(call: Call<List<Int>>, response: Response<List<Int>>) {
                listItem = response.body()
                getStory()
            }

        })
    }

    private fun getStory(){
        if (max > listItem!!.size)
            max = listItem!!.size - 1
        for (i in min..max){
            var isLast = false
            if (i == max)
                isLast = true
            fetchStory(listItem?.get(i)!!, isLast)
        }

    }

    private fun fetchStory(item: Int, isLast: Boolean) {
        val call = ApiClientRetrofit.getClient.getSrory("item/${item}.json?print=pretty")
        APIHelper.enqueueWithRetry(call, object: Callback<Story>{
            override fun onFailure(call: Call<Story>, t: Throwable) {
            }

            override fun onResponse(call: Call<Story>, response: Response<Story>) {
                listStory.add(response.body()!!)
                updateListStories(listStory, isLast)
            }
        })
    }

    private fun updateListStories(
        listStory: MutableList<Story>,
        isLast: Boolean
    ) {
        val listStory = AdapterStory(listStory)
        recycler_top_story.apply {
            layoutManager = GridLayoutManager(
                this@TopStories, 2
            )
            adapter = listStory
        }

        if (isLast){
            progressbar.visibility = View.GONE
            recycler_top_story.visibility = View.VISIBLE
        }

    }
}
