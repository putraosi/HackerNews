package com.hackernews.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hackernews.R
import com.hackernews.models.Story
import com.hackernews.screens.StoryDetail
import kotlinx.android.synthetic.main.item_card_story.view.*

/**
 * Created by Osi Novandama Putra on 27/05/20.
 */

class AdapterStory(
    private val listStory: List<Story>
) : RecyclerView.Adapter<HolderStory>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HolderStory {
        return HolderStory(
            LayoutInflater.from(p0.context).inflate(
                R.layout.item_card_story,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listStory.size
    }

    override fun onBindViewHolder(holder: HolderStory, position: Int) {
        holder.bindSaveRoutes(listStory[position])
    }
}

class HolderStory(view: View) :
    RecyclerView.ViewHolder(view) {

    fun bindSaveRoutes(
        story: Story
    ) {
        itemView.txt_title.text = story.title
        itemView.txt_score.text = "Skor ${story.score}"
        itemView.txt_comment.text = "${story.descendants} Komentar"

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, StoryDetail::class.java)
            StoryDetail.story = story
            itemView.context.startActivity(intent)
        }
    }
}
