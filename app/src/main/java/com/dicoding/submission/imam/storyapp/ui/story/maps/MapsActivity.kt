package com.dicoding.submission.imam.storyapp.ui.story.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.submission.imam.storyapp.R
import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.databinding.ActivityMapsBinding
import com.dicoding.submission.imam.storyapp.ui.story.StoryViewModel
import com.dicoding.submission.imam.storyapp.utils.SessionManager
import com.dicoding.submission.imam.storyapp.utils.ext.showToast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var _activityMapsBinding: ActivityMapsBinding? = null
    private val binding get() = _activityMapsBinding!!

    private val storyViewModel: StoryViewModel by viewModels()
    private var token: String? = null
    private lateinit var pref: SessionManager

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MapsActivity::class.java)
            context.startActivity(intent)
        }

        private val TAG = MapsActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityMapsBinding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(_activityMapsBinding?.root)

        pref = SessionManager(this)
        token = pref.getToken

        initUI()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initUI() {
        title = "Maps Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map_styles
            )
        )

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        showData()

        getMyLocation()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getMyLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun showData() {
        val boundsBuilder = LatLngBounds.Builder()

        storyViewModel.getStoryWithLocation("Bearer $token").observe(this) { response ->
            when (response) {
                is ApiResponse.Loading -> showLoading(true)
                is ApiResponse.Success -> {
                    showLoading(false)
                    if (response.data.listStory.isNotEmpty()) {
                        response.data.listStory.forEachIndexed { _, element ->
                            val lastLatLng = LatLng(element.lat, element.lon)

                            mMap.addMarker(
                                MarkerOptions().position(lastLatLng).title(element.name)
                                    .snippet(element.description)
                            )
                            boundsBuilder.include(lastLatLng)
                            val bounds: LatLngBounds = boundsBuilder.build()
                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 62))
                        }
                    } else {
                        showToast(getString(R.string.message_story_with_location_not_found))
                    }
                }
                is ApiResponse.Error -> showLoading(false)
                else -> {
                    Timber.tag("Maps").e(getString(R.string.message_unknown_error))
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }
}