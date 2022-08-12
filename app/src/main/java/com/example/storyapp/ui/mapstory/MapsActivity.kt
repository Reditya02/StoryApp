package com.example.storyapp.ui.mapstory

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.data.entity.StoryResponse
import com.example.storyapp.data.entity.UserModel
import com.example.storyapp.data.locale.LoginPreference
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.helper.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val pref = LoginPreference.getInstance(dataStore)

        val viewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            MapsViewModel::class.java
        )

        val tokenObserver = Observer<UserModel> {
            viewModel.listStory(it.token)
        }

        viewModel.getLoginStatus().observe(this, tokenObserver)


        val listObserver = Observer<StoryResponse> { response ->
            val listStory = response.listStory
            var lat = 0.0
            var lon = 0.0
            listStory.forEach {
                lat = (it.lat ?: 0) as Double
                lon = (it.lon ?: 0) as Double
                val story = LatLng(lat, lon)
                mMap.addMarker(MarkerOptions()
                    .position(story)
                    .title(it.name ?: "Anonim")
                    .snippet(it.description ?: "")
                )
            }
            mMap.moveCamera(newLatLngZoom(LatLng(lat, lon), 5f))
        }

        viewModel.apply {
            list.observe(this@MapsActivity, listObserver)
            message.observe(this@MapsActivity) {
                Toast.makeText(
                    this@MapsActivity,
                    it,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        } catch (exception: Resources.NotFoundException) {

        }
    }
}