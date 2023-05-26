package com.example.firefightersupportapp

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.location.LocationListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val LOCATION_MIN_UPDATE_TIME = 10
    private val LOCATION_MIN_UPDATE_DISTANCE = 1000

    private var mapView: MapView? = null
    private var googleMap: GoogleMap? = null
    private var location: Location? = null

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            drawMarker(location, getText(R.string.i_am_here).toString())
            locationManager!!.removeUpdates(this)
        }

        override fun onStatusChanged(s: String?, i: Int, bundle: Bundle?) {}
    }

    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        supportActionBar!!.title = "Moja lokalizacja"
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        initView(savedInstanceState)
    }

    private fun initView(savedInstanceState: Bundle?) {
        mapView = findViewById(R.id.mapView)
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync { googleMap -> onMapReady(googleMap) }
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
        getCurrentLocation()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    private fun initMap() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (googleMap != null) {
                googleMap!!.isMyLocationEnabled = true
                googleMap!!.uiSettings.isMyLocationButtonEnabled = true
                googleMap!!.uiSettings.setAllGesturesEnabled(true)
                googleMap!!.uiSettings.isZoomControlsEnabled = true
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    12
                )
            }
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                    13
                )
            }
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val isGPSEnabled: Boolean =
                locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled: Boolean =
                locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(
                    applicationContext,
                    getText(R.string.provider_failed),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                location = null
                if (isGPSEnabled) {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        LOCATION_MIN_UPDATE_TIME.toLong(),
                        LOCATION_MIN_UPDATE_DISTANCE.toFloat(),
                        locationListener
                    )
                    location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                }
                if (isNetworkEnabled) {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        LOCATION_MIN_UPDATE_TIME.toLong(),
                        LOCATION_MIN_UPDATE_DISTANCE.toFloat(),
                        locationListener
                    )
                    location =
                        locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }
                if (location != null) {
                    drawMarker(location!!, getText(R.string.i_am_here).toString())
                }
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    12
                )
            }
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                    13
                )
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        initMap()
        getCurrentLocation()
    }

    private fun drawMarker(location: Location, title: String) {
        if (googleMap != null) {
            googleMap!!.clear()
            val latLng = LatLng(location.latitude, location.longitude)
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title(title)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            googleMap!!.addMarker(markerOptions)
            googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            googleMap!!.animateCamera(CameraUpdateFactory.zoomTo(12f))
        }
    }

}