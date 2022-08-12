package com.example.storyapp.ui.liststory

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityListStoryBinding
import com.example.storyapp.ui.addstory.AddStoryActivity
import com.example.storyapp.ui.logout.LogoutActivity
import com.example.storyapp.ui.mapstory.MapsActivity

class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding

    private val viewModel: ListStoryViewModel by viewModels {
        ListStoryViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showRecycler()
    }

    private fun showRecycler() {
        val storyAdapter = StoryAdapter()
        binding.rvListStory.apply {
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingAdapter {
                    storyAdapter.retry()
                }
            )
            layoutManager = LinearLayoutManager(this@ListStoryActivity)
        }

        viewModel.story.observe(this) {
            storyAdapter.setList(it)
            storyAdapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.logout -> {
                val intent = Intent(this, LogoutActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.add_story -> {
                val intent = Intent(this, AddStoryActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.map -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@ListStoryActivity, LogoutActivity::class.java)
        startActivity(intent)
    }
}