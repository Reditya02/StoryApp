package com.example.storyapp.ui.logout

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.entity.UserModel
import com.example.storyapp.data.locale.LoginPreference
import com.example.storyapp.helper.Const.TOKEN
import kotlinx.coroutines.launch

class LogoutViewModel(private val pref: LoginPreference) : ViewModel() {

    fun getLoginStatus(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun deleteLogin() {
        viewModelScope.launch {
            pref.logout()
            TOKEN = ""
        }
    }
}