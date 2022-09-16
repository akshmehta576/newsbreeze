package com.practice.greedygame.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practice.greedygame.R
import com.practice.greedygame.domain.model.Article

class SaveNewsAdapter(private val context: Context, private val navController: NavController) :
    RecyclerView.Adapter<SaveNewsAdapter.SaveNewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveNewsViewHolder {
        Log.i("offlinedataone", "it.toString()")

        return SaveNewsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_save_news, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SaveNewsViewHolder, position: Int) {
        val news = differ.currentList[position]
        Log.i("offlinedata1", news.toString())
        holder.newsHeadLine.text = news.title
        holder.newsDate.text =
            news.publishedAt?.subSequence(8, 10).toString() + "-" + news.publishedAt?.subSequence(
                5,
                7
            )
                .toString() + "-" + news.publishedAt?.subSequence(0, 4).toString()+" "+news.author
        Glide.with(holder.itemView)
            .load(news.urlToImage)
            .into(holder.newsImg)



        holder.itemView.setOnClickListener {
            val bundle = bundleOf(
                "newsImg" to news.urlToImage,
                "newsTitle" to news.title,
                "newsDate" to news.publishedAt,
                "newsContent" to news.content,
                "sourceName" to news.source?.name,
                "authorName" to news.author
            )
            navController.navigate(R.id.action_savedNewsFragment_to_readNewsFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        Log.i("offlinedata", differ.currentList.size.toString())
        return differ.currentList.size
    }


    class SaveNewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var newsImg: ImageView = itemView.findViewById(R.id.image_news)
        var newsHeadLine: TextView = itemView.findViewById(R.id.text_headline_news)
        var newsDate: TextView = itemView.findViewById(R.id.text_date_news)

    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
}