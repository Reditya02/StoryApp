package com.example.storyapp.ui.splashscreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.helper.Const.TAG
import com.example.storyapp.helper.Const.TIME_SPLASH
import com.example.storyapp.data.entity.UserModel
import com.example.storyapp.data.locale.LoginPreference
import com.example.storyapp.databinding.ActivitySplashScreenBinding
import com.example.storyapp.ui.liststory.ListStoryActivity
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.helper.Const.TOKEN
import com.example.storyapp.helper.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = LoginPreference.getInstance(dataStore)

        val viewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SplashScreenViewModel::class.java
        )

        Handler(mainLooper).postDelayed({
            val loginObserver = Observer<UserModel> {
                Log.d(TAG, it.toString())

                val isLogin = it.isLogin
                val token = it.token

                if (isLogin) {
                    TOKEN = token
                    val intent = Intent(this, ListStoryActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }

            viewModel.getLoginStatus().observe(this, loginObserver)
        }, TIME_SPLASH)

        supportActionBar?.hide()
    }
}