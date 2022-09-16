package com.practice.greedygame.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practice.greedygame.R
import com.practice.greedygame.domain.model.NewsResponse
import com.practice.greedygame.presentation.adapter.NewsAdapter.NewsViewHolder
import com.practice.greedygame.presentation.viewmodel.NewsViewModel

class NewsAdapter(
    private val context: Context,
    private val listofNews: NewsResponse,
    private val navController: NavController,
    private val newsViewModel: NewsViewModel
) :
    RecyclerView.Adapter<NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var newsImg: ImageView = itemView.findViewById(R.id.image_news)
        var newsHeadLine: TextView = itemView.findViewById(R.id.text_headline_news)
        var newsDescription: TextView = itemView.findViewById(R.id.text_description_news)
        var newsDate: TextView = itemView.findViewById(R.id.text_date_news)
        var saveBtn: TextView = itemView.findViewById(R.id.saveBtn)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_news, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = listofNews.articles[position]

        holder.newsHeadLine.text = news.title
        holder.newsDescription.text = news.description
        holder.newsDate.text =
            news.publishedAt?.subSequence(8, 10).toString() + "-" + news.publishedAt?.subSequence(
                5,
                7
            )
                .toString() + "-" + news.publishedAt?.subSequence(0, 4).toString()
        Glide.with(holder.itemView)
            .load(news.urlToImage)
            .into(holder.newsImg)

        holder.saveBtn.setOnClickListener {
            newsViewModel.saveArticle(news)

            Toast.makeText(context, "Saved for read later!", Toast.LENGTH_SHORT).show()

        }

        holder.itemView.setOnClickListener {
            val bundle = bundleOf(
                "newsImg" to news.urlToImage,
                "newsTitle" to news.title,
                "newsDate" to news.publishedAt,
                "newsContent" to news.content,
                "sourceName" to news.source?.name,
                "authorName" to news.author,
                "saveornot" to news.isSave
            )
            navController.navigate(R.id.action_newsFragment_to_readNewsFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return listofNews.articles.size;
    }
}




