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

package com.ericafenyo.bikediary.ui.map

import android.Manifest.permission
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.databinding.FragmentMapBinding
import com.ericafenyo.bikediary.util.LocationHelper
import com.ericafenyo.tracker.analysis.MetricsManager
import com.ericafenyo.tracker.datastore.RecordsProvider
import com.ericafenyo.tracker.util.PermissionsManager
import com.ericafenyo.tracker.util.getExplicitIntent
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Line
import com.mapbox.mapboxsdk.plugins.annotation.LineManager
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {
  private val mapModel: MapViewModel by viewModels()
  private val binding: FragmentMapBinding by dataBinding()

  private lateinit var locationComponent: LocationComponent
  private lateinit var mapbox: MapboxMap
  private var line: Line? = null
  @Inject lateinit var provider: RecordsProvider
  private lateinit var chronometerManager: ChronometerManager

  private val capturePhoto =
    registerForActivityResult(ActivityResultContracts.TakePicture()) { bitmap -> // Now this bitmap can be used wherever we want
      Timber.d("bittt: $bitmap")
    }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.lifecycleOwner = viewLifecycleOwner
    binding.model = mapModel

    binding.mapView.onCreate(savedInstanceState)

    binding.trackingEvent = object : OnTrackingEvent {
      override fun onClick(isOngoing: Boolean) {
        if (isOngoing) {
          requireActivity()
            .sendBroadcast(requireActivity().getExplicitIntent(R.string.tracker_action_stop))
          chronometerManager.stop()
        } else {
          requireActivity()
            .sendBroadcast(requireActivity().getExplicitIntent(R.string.tracker_action_start))
          chronometerManager.start()
        }
      }

      override fun takePicture() {
        capturePhoto.launch(null)
      }

      override fun stopTracking() {
        requireActivity().apply { sendBroadcast(getExplicitIntent(R.string.tracker_action_stop)) }
        chronometerManager.stop()
      }

      override fun startTracking() {
        requireActivity().apply { sendBroadcast(getExplicitIntent(R.string.tracker_action_start)) }
        chronometerManager.stop()
        chronometerManager.start()
      }

      override fun showDeviceLocation() {
        LocationHelper.getLastLocation(requireContext()) { location ->
          if (location != null) {
            zoomOnLocation(location)
          }
        }
      }
    }

    binding.mapView.getMapAsync(this)

    displayMetrics()
    chronometerManager = ChronometerManager(binding.liveMetrics.chronometer)
  }

  override fun onMapReady(mapbox: MapboxMap) {
    this.mapbox = mapbox
    this.locationComponent = mapbox.locationComponent
    prepareMap(mapbox)
  }

  private fun getDeviceLocation(style: Style) {
    if (PermissionsManager.isForegroundLocationPermissionGranted(requireContext())) {
      enableLocationComponent(style)
    } else {
      lifecycleScope.launchWhenResumed {
        requestForegroundLocationPermission { isGranted ->
          if (isGranted) {
            enableLocationComponent(style)
          }
        }
      }
    }
  }

  private fun prepareMap(mapbox: MapboxMap) {
    mapbox.setStyle(Style.MAPBOX_STREETS) { style -> getDeviceLocation(style) }
  }

  private fun requestForegroundLocationPermission(block: (Boolean) -> Unit) {
    registerForActivityResult(RequestPermission()) { isGranted -> block(isGranted) }
      .launch(permission.ACCESS_FINE_LOCATION)
  }

  private fun displayMetrics() {
    lifecycleScope.launchWhenCreated {
      provider.provideRecords().collect { records ->
        val locations = records.map { it.location }
        val metrics = MetricsManager.getLiveMetrics(locations)
        Timber.d("Metrics: $metrics")
        binding.metrics = metrics
      }
    }
  }

  @SuppressLint("MissingPermission")
  private fun enableLocationComponent(style: Style) {
    val locationComponentOptions = LocationComponentOptions.builder(requireContext())
      .pulseEnabled(true)
      .pulseColor(ContextCompat.getColor(requireContext(), R.color.color_primary))
      .build()

    // Activate with options
    locationComponent.activateLocationComponent(
      LocationComponentActivationOptions.builder(requireContext(), style)
        .locationComponentOptions(locationComponentOptions)
        .build()
    )

    // Set the component's camera mode
    locationComponent.cameraMode = CameraMode.NONE

    // Set the component's render mode
    locationComponent.renderMode = RenderMode.NORMAL

    // Enable to make component visible
    locationComponent.isLocationComponentEnabled = true

    locationComponent.zoomWhileTracking(15.0)

    if (locationComponent.isLocationComponentEnabled) {
      LocationHelper.getLastLocation(requireContext()) { lastLocation ->
        if (lastLocation != null) {
          zoomOnLocation(lastLocation)
        } else {
          LocationHelper.getCurrentLocation(requireContext()) { currentLocation ->
            if (currentLocation != null) {
              zoomOnLocation(currentLocation)
            }
          }
        }
      }
    }

    setupTrackingIndicator(style)
  }

  private fun setupTrackingIndicator(style: Style) {
    val lineManager = LineManager(binding.mapView, mapbox, style)

    mapModel.isTrackingOngoing.observe(viewLifecycleOwner, { isOngoing ->
      val coordinatesList = mutableListOf<LatLng>()
      val onIndicatorPositionChangedListener = { point: Point ->
        val coordinates = LatLng(point.latitude(), point.longitude())
        coordinatesList.add(coordinates)
        val lineOptions = LineOptions()
          .withLineColor("#4c91df")
          .withLineWidth(5f)
          .withLatLngs(coordinatesList)
        if (line != null) {
          line?.latLngs = coordinatesList
          if (lineManager.annotations.isEmpty) {
            line = lineManager.create(lineOptions)
          } else {
            lineManager.update(line)
          }
        } else {
          line = lineManager.create(lineOptions)
        }

        val cameraPosition = LatLng(point.latitude(), point.longitude())
        navigateCamera(mapbox, cameraPosition)
      }
      if (isOngoing) {
        locationComponent.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
      } else {
        locationComponent.removeOnIndicatorPositionChangedListener(
          onIndicatorPositionChangedListener
        )
      }
    })
  }

  private fun zoomOnLocation(point: Point, zoomIn: Boolean = true) {
    if (zoomIn) {
      navigateCamera(mapbox, LatLng(point.latitude(), point.longitude()))
    }
  }

  private fun zoomOnLocation(location: Location) {
    navigateCamera(mapbox, LatLng(location.latitude, location.longitude))
  }

  private fun navigateCamera(map: MapboxMap, latLng: LatLng) {
    val position = CameraPosition.Builder()
      .target(latLng) // Sets the new camera position
      .zoom(15.0) // Sets the zoom
      .build() // Creates a CameraPosition from the builder
    map.easeCamera(CameraUpdateFactory.newCameraPosition(position))
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

  override fun onLowMemory() {
    super.onLowMemory()
    binding.mapView.onLowMemory()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.mapView.onDestroy()
  }
}
