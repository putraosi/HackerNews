package com.hackernews.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackernews.R
import com.hackernews.adapters.AdapterStoryDetail
import com.hackernews.models.Comment
import com.hackernews.models.Story
import com.hackernews.models.User
import com.hackernews.services.APIHelper
import com.hackernews.services.ApiClientRetrofit
import kotlinx.android.synthetic.main.activity_story_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class StoryDetail : AppCompatActivity() {

    val listComment = mutableListOf<Comment>()
    var min = 0
    var max = 19
    var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_detail)

        init()
        actionListener()
    }

    private fun actionListener() {
        img_favorite.setOnClickListener {
            if (isFavorite) {
                isFavorite = false
                img_favorite.setBackgroundResource(R.drawable.ic_unfavorite)
            } else {
                isFavorite = true
                img_favorite.setBackgroundResource(R.drawable.ic_favorite)
            }
        }
    }

    private fun init() {
        toolbar_text.text = "Story Detail"
        setSupportActionBar(toolbar)

        progressbar.visibility = View.VISIBLE
        panel_content.visibility = View.INVISIBLE

        fetchUser()
        getComment(story!!.kids)

        story?.let {

            val time = it.time.toLong()
            val sdf = SimpleDateFormat("dd/mm/yyyy")
            val newDate = Date(time)

            txt_title.text = it.title
            txt_by.text = "By ${it.by}"
            txt_date.text = sdf.format(newDate)
        }

        if (isFavorite) {
            img_favorite.setBackgroundResource(R.drawable.ic_favorite)
        } else {
            img_favorite.setBackgroundResource(R.drawable.ic_unfavorite)
        }
    }

    private fun fetchUser() {
        val call = ApiClientRetrofit.getClient.getUser("user/${story!!.by}.json?print=pretty")
        APIHelper.enqueueWithRetry(call, object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                response.body().let {
                    if (!it!!.about.isNullOrEmpty()) {
                        val content = it.about
                        txt_description.text =
                            HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    } else {
                        txt_description.text = "-"
                    }
                }
            }
        })
    }

    private fun getComment(kids: List<Int>) {
        if (max > kids.size)
            max = kids.size - 1

        Log.e("CEK", max.toString())
        for (i in min..max) {
            var isLast = false
            if (i == max)
                isLast = true
            fetchComment(kids[i], isLast)
        }
    }

    private fun fetchComment(id: Int, isLast: Boolean) {
        val call = ApiClientRetrofit.getClient.getComment("item/${id}.json?print=pretty")
        APIHelper.enqueueWithRetry(call, object : Callback<Comment> {
            override fun onFailure(call: Call<Comment>, t: Throwable) {

            }

            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                listComment.add(response.body()!!)
                updateListComment(listComment, isLast)
            }
        })
    }

    private fun updateListComment(
        listComment: MutableList<Comment>,
        isLast: Boolean
    ) {
        val adapterStoryDetail = AdapterStoryDetail(listComment)
        recycler_comments.apply {
            layoutManager = LinearLayoutManager(this@StoryDetail)
            adapter = adapterStoryDetail
        }

        if (isLast) {
            progressbar.visibility = View.GONE
            panel_content.visibility = View.VISIBLE
        }
    }

    companion object {
        var story: Story? = null
    }
}
