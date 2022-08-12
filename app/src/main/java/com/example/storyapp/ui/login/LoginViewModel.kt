package com.example.storyapp.ui.login

import androidx.lifecycle.*
import com.example.storyapp.data.entity.LoginResponse
import com.example.storyapp.data.locale.LoginPreference
import com.example.storyapp.data.entity.UserModel
import com.example.storyapp.data.remote.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: LoginPreference) : ViewModel() {
    private val _response = MutableLiveData<LoginResponse>()
    var response: LiveData<LoginResponse> = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loginUser(email: String, password: String) {
        _isLoading.postValue(true)
        val client = ApiConfig.getApiService().loginUser(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    _response.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.postValue(false)
            }
        })
    }

    fun saveLogin(user: UserModel) {
        viewModelScope.launch {
            pref.login(user)
        }
    }
}