package com.hackernews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.hackernews.R
import com.hackernews.models.Comment
import kotlinx.android.synthetic.main.item_comment.view.*

/**
 * Created by Osi Novandama Putra on 27/05/20.
 */

class AdapterStoryDetail(
    private val listComment: List<Comment>
) : RecyclerView.Adapter<HolderStoryDetail>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HolderStoryDetail {
        return HolderStoryDetail(
            LayoutInflater.from(p0.context).inflate(
                R.layout.item_comment,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listComment.size
    }

    override fun onBindViewHolder(holder: HolderStoryDetail, position: Int) {
        holder.bindSaveRoutes(listComment[position])
    }
}

class HolderStoryDetail(view: View) :
    RecyclerView.ViewHolder(view) {

    fun bindSaveRoutes(
        comment: Comment
    ) {
        val content = comment.text
        itemView.txt_comment.text = HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}
