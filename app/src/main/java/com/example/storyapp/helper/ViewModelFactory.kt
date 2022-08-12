package com.example.storyapp.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.locale.LoginPreference
import com.example.storyapp.ui.login.LoginViewModel
import com.example.storyapp.ui.logout.LogoutViewModel
import com.example.storyapp.ui.mapstory.MapsViewModel
import com.example.storyapp.ui.splashscreen.SplashScreenViewModel

class ViewModelFactory(private val pref: LoginPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(pref) as T
            modelClass.isAssignableFrom(LogoutViewModel::class.java) -> LogoutViewModel(pref) as T
            modelClass.isAssignableFrom(SplashScreenViewModel::class.java) -> SplashScreenViewModel(pref) as T
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> MapsViewModel(pref) as T
            else -> throw IllegalArgumentException("ViewModel Class not found")
        }

    }
}