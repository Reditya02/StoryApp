package com.example.storyapp.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.entity.RegisterResponse
import com.example.storyapp.data.locale.LoginPreference
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = LoginPreference.getInstance(dataStore)

        val viewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            RegisterViewModel::class.java
        )

        binding.apply {
            btnRegister.setOnClickListener {
                if (isTextEmpty(edtName))
                    edtName.error = "Masukkan nama"
                else if (isTextEmpty(edtEmail))
                    edtEmail.error = "Masukkan email"
                else if (isTextEmpty(edtPassword))
                    edtPassword.error = "Masukkan password"
                else {
                    if (
                        edtName.error.isNullOrEmpty() &&
                        edtEmail.error.isNullOrEmpty() &&
                        edtPassword.error.isNullOrEmpty()
                    ) {
                        viewModel.registerUser(
                            edtName.text.toString(),
                            edtEmail.text.toString(),
                            edtPassword.text.toString()
                        )

                        val observerResponse = Observer<RegisterResponse> {
                            val error = it.error ?: true
                            val message = it.message ?: "Gagal membuat akun"
                            if (error)
                                makeToast(message)
                            else {
                                makeToast("Berhasil membuat akun")

                                val intent = Intent(
                                    this@RegisterActivity,
                                    LoginActivity::class.java
                                )
                                startActivity(intent)
                            }
                        }

                        viewModel.response.observe(this@RegisterActivity, observerResponse)

                        viewModel.isLoading.observe(this@RegisterActivity) {
                            showLoading(it)
                        }
                    }
                }
            }

            btnLogin.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
            }
        }
    }

    private fun makeToast(message: String) {
        Toast.makeText(
            this@RegisterActivity,
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}