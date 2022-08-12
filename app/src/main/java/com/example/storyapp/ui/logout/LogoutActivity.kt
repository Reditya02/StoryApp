package com.example.storyapp.ui.logout

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.locale.LoginPreference
import com.example.storyapp.databinding.ActivityLogoutBinding
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.helper.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LogoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = LoginPreference.getInstance(dataStore)

        val viewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            LogoutViewModel::class.java
        )

        viewModel.getLoginStatus().observe(this) {
            binding.apply {
                tvName.text = it.name
                tvEmail.text = it.email
            }
        }

        binding.apply {
            btnYes.setOnClickListener {
                viewModel.deleteLogin()
                val intent = Intent(this@LogoutActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
            }

            btnNo.setOnClickListener {
                onBackPressed()
            }
        }
    }


}