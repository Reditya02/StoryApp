package com.example.storyapp.ui.detailstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.data.entity.ListStoryItem
import com.example.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY)

        binding.apply {
            Glide.with(this@DetailStoryActivity)
                .load(story?.photoUrl)
                .into(imgPhoto)

            tvName.text = story?.name
            tvDescription.text = story?.description
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}