/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2021 Eric Afenyo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ericafenyo.habitdiary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ericafenyo.habitdiary.databinding.FragmentMapBinding
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.wada811.databinding.dataBinding


class MapFragment : Fragment(R.layout.fragment_map) {
  private val binding: FragmentMapBinding by dataBinding()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    Mapbox.getInstance(requireContext(), BuildConfig.MAPBOX_ACCESS_TOKEN)
    binding.mapView.onCreate(savedInstanceState)
    binding.mapView.getMapAsync { map -> setupMap(map) }
  }

  private fun setupMap(map: MapboxMap) {
    map.setStyle(Style.OUTDOORS) { style ->
      val nantesCoordinates = LatLng(47.2192144, -1.5942779)
      navigateCamera(map, nantesCoordinates)
    }
  }

  private fun navigateCamera(map: MapboxMap, latLng: LatLng) {
    val position = CameraPosition.Builder()
      .target(latLng) // Sets the new camera position
      .zoom(11.0) // Sets the zoom
      .build() // Creates a CameraPosition from the builder

    map.easeCamera(CameraUpdateFactory.newCameraPosition(position), 10)
  }

  override fun onStart() {
    super.onStart()
    binding.mapView.onStart()
  }

  override fun onResume() {
    super.onResume()
    binding.mapView.onResume()
  }

  override fun onPause() {
    super.onPause()
    binding.mapView.onPause()
  }

  override fun onStop() {
    super.onStop()
    binding.mapView.onStop()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    //binding.mapView.onSaveInstanceState(outState)
  }

  override fun onLowMemory() {
    super.onLowMemory()
    binding.mapView.onLowMemory()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.mapView.onDestroy()
  }

  override fun onDestroy() {
    super.onDestroy()
    binding.mapView.onDestroy()
  }
}
