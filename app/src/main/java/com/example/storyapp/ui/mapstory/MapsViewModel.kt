package com.example.storyapp.ui.mapstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.entity.StoryResponse
import com.example.storyapp.data.entity.UserModel
import com.example.storyapp.data.locale.LoginPreference
import com.example.storyapp.data.remote.ApiConfig
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val pref: LoginPreference) : ViewModel() {
    private val _list = MutableLiveData<StoryResponse>()
    var list: LiveData<StoryResponse> = _list

    private val _message = MutableLiveData<String>()
    var message: LiveData<String> = _message

    fun listStory(token: String) {
        val client = ApiConfig.getApiService().getStoryWithLocation(token)

        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody?.error == true) {
                        val error = responseBody.error.toString()
                        _message.value = error
                    }

                    responseBody?.let {
                        _list.value = it
                    }
                } else {
                    val error = response.errorBody()?.string()?.let {
                        JSONObject(it)
                    }?.getString("message")
                    _message.value = error.toString()
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _message.value = t.message
            }
        })
    }

    fun getLoginStatus(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}