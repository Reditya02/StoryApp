package com.example.storyapp.ui.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.entity.UserModel
import com.example.storyapp.data.locale.LoginPreference

class SplashScreenViewModel(private val pref: LoginPreference): ViewModel() {
    fun getLoginStatus(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}