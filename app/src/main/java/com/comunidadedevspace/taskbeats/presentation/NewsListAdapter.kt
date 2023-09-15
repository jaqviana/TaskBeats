package com.comunidadedevspace.taskbeats.presentation


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.local.News


class NewsListAdapter : ListAdapter<News, NewsListViewHolder>(NewsListAdapter) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsListViewHolder(view)
    }


    override fun onBindViewHolder(holder: NewsListViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
    }


    //para calcular as diferenças entre duas listas de objetos, determinar se dois itens são iguais em termos de identificação e conteúdo, para otimizar as atualizações no RecyclerView quando os dados são alterados.
    companion object : DiffUtil.ItemCallback<News>() {

        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem

        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.imgUrl == newItem.imgUrl

        }

    }

}


    class NewsListViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {

        private val tvNews = view.findViewById<TextView>(R.id.tv_news)
        private val imgNews = view.findViewById<ImageView>(R.id.imageView)
        fun bind(
            news: News

            ) {
            tvNews.text = news.title
            imgNews.load(news.imgUrl){
                transformations(RoundedCornersTransformation(12f))
            }

        }
    }
