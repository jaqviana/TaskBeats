package com.comunidadedevspace.taskbeats.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comunidadedevspace.taskbeats.data.remote.NewsDto
import com.comunidadedevspace.taskbeats.data.remote.NewsService
import com.comunidadedevspace.taskbeats.data.remote.RetrofitModule
import kotlinx.coroutines.launch
import java.lang.Exception

class NewsListViewModel(
    //vai ter uma dependencia no NewsService
    private val newsService: NewsService
) : ViewModel() {

    // Service API
    // ViewModel
    // View/Activiy
    //DTO eh so pra pegar um objeto e trazer pra ca
    private val _newsListLiveData = MutableLiveData<List<NewsDto>>() //o undersicore _ eh p pode alterar o valor (mutable)
    val newsListLiveData: LiveData<List<NewsDto>> = _newsListLiveData

    //inicializar o viewmodel
    init {
        getNewsList()
    }

    //chamar o backend, qdo tiver a resposta atualiza o livedata quem tiver observando o livedata mostra a atulizacao
    private fun getNewsList() {
        viewModelScope.launch {//viewModelScope qdo a activity parar de observar o viewmodel sabe disso - live cicle awareness, ele sabe o lifecicle da activity para nao disperdicar memoria
           //try catch se tiver um erro de internet ele vai me dar um erro e pra evitar q da um crache no app
            try {
                val topNews = newsService.fetchTopNews().data
                val allNews = newsService.fetchAllNews().data
                _newsListLiveData.value = topNews + allNews
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    companion object {

        fun create(): NewsListViewModel {
            val newsService = RetrofitModule.createNewsService()
            return NewsListViewModel(newsService)
        }
    }
}
