package com.example.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.storyapp.data.entity.ListStoryItem
import com.example.storyapp.data.remote.ApiService

class StoryRepository(private val apiService: ApiService) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }
}