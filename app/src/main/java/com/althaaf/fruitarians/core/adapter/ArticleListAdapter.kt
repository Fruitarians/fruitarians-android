package com.althaaf.fruitarians.core.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.althaaf.fruitarians.core.data.network.dashboard.article.ResultItem
import com.althaaf.fruitarians.databinding.ItemArticleBinding
import com.althaaf.fruitarians.ui.article.DetailArticleActivity
import com.bumptech.glide.Glide

class ArticleListAdapter(private val listArticles: List<ResultItem>): RecyclerView.Adapter<ArticleListAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: ItemArticleBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResultItem) {
            binding.titleArticle.text = item.title
            binding.descArticle.text = item.konten
            Glide.with(itemView.context)
                .load(item.photo)
                .into(binding.imageArticle)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailArticleActivity::class.java)
                intent.putExtra(DetailArticleActivity.EXTRA_ID, item.id)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  MyViewHolder(binding)
    }

    override fun getItemCount() = listArticles.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = listArticles[position]
        holder.bind(item)
    }
}