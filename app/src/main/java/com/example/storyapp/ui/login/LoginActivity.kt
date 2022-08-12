package com.example.storyapp.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.data.entity.LoginResponse
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.register.RegisterActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.locale.LoginPreference
import com.example.storyapp.data.entity.UserModel
import com.example.storyapp.ui.liststory.ListStoryActivity
import com.example.storyapp.helper.Const.TOKEN
import com.example.storyapp.helper.ViewModelFactory
import kotlin.system.exitProcess

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = LoginPreference.getInstance(dataStore)

        val viewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            LoginViewModel::class.java
        )

        binding.apply {
            btnLogin.setOnClickListener {

                if (isTextEmpty(edtEmail))
                    edtEmail.error = "Masukkan email"
                else if (isTextEmpty(edtPassword))
                    edtPassword.error = "Masukkan password"
                else {
                    if (
                        edtEmail.error.isNullOrEmpty() &&
                        edtPassword.error.isNullOrEmpty()
                    ) {
                        viewModel.loginUser(
                            edtEmail.text.toString(),
                            edtPassword.text.toString()
                        )

                        val observerResponse = Observer<LoginResponse> {
                            val error = it.error ?: true
                            val message = it.message ?: "Gagal masuk"
                            if (error)
                                makeToast(message)
                            else {
                                makeToast("Berhasil masuk")

                                TOKEN = "Bearer ${it.loginResult?.token}"

                                val user = UserModel(
                                    it.loginResult?.name.toString(),
                                    edtEmail.text.toString(),
                                    edtPassword.text.toString(),
                                    true,
                                    TOKEN
                                )
                                viewModel.saveLogin(user)

                                val intent = Intent(
                                    this@LoginActivity,
                                    ListStoryActivity::class.java
                                )
                                startActivity(intent)
                            }
                        }

                        viewModel.response.observe(this@LoginActivity, observerResponse)

                        viewModel.isLoading.observe(this@LoginActivity) {
                            showLoading(it)
                        }
                    }
                }
            }

            btnRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun makeToast(message: String) {
        Toast.makeText(
            this@LoginActivity,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun isTextEmpty(editText: EditText): Boolean {
        val text = editText.text.toString().trim()
        if (text.isEmpty())
            return true
        return false
    }
    override fun onBackPressed() {
        this.finishAffinity()
        exitProcess(-1)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}