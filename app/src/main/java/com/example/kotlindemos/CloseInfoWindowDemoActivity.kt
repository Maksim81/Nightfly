/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.kotlindemos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlindemos.OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener
import com.example.kotlindemos.R.drawable.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*


/**
 * This shows how to close the info window when the currently selected marker is re-tapped.
 */
class CloseInfoWindowDemoActivity :
        AppCompatActivity(),
        OnGlobalLayoutAndMapReadyListener {

    private lateinit var map: GoogleMap

    /** Keeps track of the selected marker. It will be set to null if no marker is selected. */
    private var selectedMarker: Marker? = null

    /**
     * If user tapped on the the marker which was already showing info window,
     * the showing info window will be closed. Otherwise will show a different window.
     */
    private val markerClickListener = object : GoogleMap.OnMarkerClickListener {
        override fun onMarkerClick(marker: Marker?): Boolean {
            if (marker == selectedMarker) {
                selectedMarker = null
                // Return true to indicate we have consumed the event and that we do not
                // want the the default behavior to occur (which is for the camera to move
                // such that the marker is centered and for the marker's info window to open,
                // if it has one).
                return true
            }

            selectedMarker = marker
            // Return false to indicate that we have not consumed the event and that
            // we wish for the default behavior to occur.
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marker_close_info_window_on_retap_demo)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        OnMapAndViewReadyListener(mapFragment, this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        // Return if googleMap was null
        map = googleMap ?: return

        with(map) {
            uiSettings.isZoomControlsEnabled = false

            setOnMarkerClickListener(markerClickListener)

            // Set listener for map click event. Any showing info window closes
            // when the map is clicked. Clear the currently selected marker.
            setOnMapClickListener { selectedMarker = null }

            setContentDescription(getString(R.string.close_info_window_demo_details))

            // Add markers to different cities in Australia and include it in bounds
            val bounds = LatLngBounds.Builder()
            cities.map { city ->
                addMarker(MarkerOptions().apply {
                    position(city.latLng)
                    title(city.title)
                    snippet(city.snippet)
                    icon(BitmapDescriptorFactory.fromResource(getStarByTitle(city.title)))
                })
                bounds.include(city.latLng)
            }

            moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50))
        }
    }

    private fun getStarByTitle(title: String): Int {

        if (title == "Venus Klubi") return R.drawable.ic_club;
        else return R.drawable.star_club;

    }

    /**
     * Class to contain information about a marker.
     *
     * @property latLng latitude and longitude of the marker
     * @property title a string containing the city name
     * @property snippet a string containing the population of the city
     */
    class SelectStarByTitle

    class MarkerInfo(val latLng: LatLng, val title: String, val snippet: String)

    private val cities = listOf(
            MarkerInfo(LatLng(59.4365480, 24.7489300),
                    "Paparazzi", "Viru 18"),
            MarkerInfo(LatLng(59.4370841, 24.7526080),
                    "Venus Klubi", "Vana-Viru 14"),
            MarkerInfo(LatLng(59.4309197, 24.7442982),
                    "Jamaica baar (Karaoke)", "Roosikrantsi 15"),
            MarkerInfo(LatLng(59.4351784, 24.7452193),
                    "Jamaica baar (Karaoke)", "Roosikrantsi 15"),
            MarkerInfo(LatLng(59.4529034, 24.8753043),
                    "Well klubi", "Läänemere tee 28")/*,
            MarkerInfo(LatLng(-31.952854, 115.857342),
                    "Perth", "Population: 1,738,800")*/
    )
}
