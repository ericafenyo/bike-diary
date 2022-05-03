/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2022 Eric Afenyo
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

package com.ericafenyo.bikediary.ui.screens.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ericafenyo.bikediary.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment() {
  private val viewModel: MapViewModel by viewModels()

//  private lateinit var locationComponent: LocationComponent
//  private lateinit var mapboxMap: MapboxMap
//  private var line: Line? = null
//  @Inject lateinit var provider: RecordsProvider

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return ComposeView(requireContext()).apply {
      setContent {
        AppTheme {
          Map(dispatch = { action -> viewModel.dispatch(action) })
        }
      }
    }
  }

//  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//    super.onViewCreated(view, savedInstanceState)
//
////    mapModel.action.observe(viewLifecycleOwner, EventObserver { action ->
////      when (action) {
////        START_TRACKING -> {
////          requireActivity()
////            .sendBroadcast(requireActivity().getExplicitIntent(string.tracker_action_start))
////        }
////
////        END_TRACKING -> {
////          requireActivity()
////            .sendBroadcast(requireActivity().getExplicitIntent(string.tracker_action_stop))
////        }
////        LAUNCH_CAMERA -> {
////          Timber.d("Launch camera")
////        }
////      }
////    })
//
//    binding.mapView.getMapAsync { map ->
//      mapboxMap = map
//      locationComponent = map.locationComponent
//      setupMap(requireActivity(), map)
//    }
//
//    /*binding.fabShowCurrentLocation.setOnClickListener {
//      com.ericafenyo.tracker.util.PermissionsManager.isForegroundLocationPermissionGranted()
//    }*/
//
//    displayMetrics()
//  }


//  @SuppressLint("MissingPermission")
//  private fun setupMap(context: Context, map: MapboxMap) {
//    map.setStyle(Style.MAPBOX_STREETS) { style ->
//      // Show the current device location
//      if (PermissionsManager.isForegroundLocationPermissionGranted(requireContext())) {
//        enableLocationComponent(requireContext(), style)
//        locationComponent.addOnIndicatorPositionChangedListener(positionChangeListener)
//
//      } else {
//        locationRequestContract.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//      }
//
//      binding.fabShowCurrentLocation.setOnClickListener {
//        isZoomedIn.set(true)
//      }
//
//      val lineManager = LineManager(binding.mapView, map, style)
//      val coordinatesList = mutableListOf<LatLng>()
//      mapModel.isTrackingOngoing.observe(viewLifecycleOwner) { isOngoing ->
//        enableLocationComponent(context, style)
//
//        locationComponent.addOnIndicatorPositionChangedListener { point ->
//          if (isOngoing) {
//            val coordinates = LatLng(point.latitude(), point.longitude())
//            coordinatesList.add(coordinates)
//            val lineOptions = LineOptions()
//              .withLineColor("#4c91df")
//              .withLineWidth(7f)
//              .withLatLngs(coordinatesList)
//            if (line != null) {
//              line?.latLngs = coordinatesList
//              if (lineManager.annotations.isEmpty) {
//                line = lineManager.create(lineOptions)
//              } else {
//                lineManager.update(line)
//              }
//            } else {
//              line = lineManager.create(lineOptions)
//            }
//
//            val cameraPosition = LatLng(point.latitude(), point.longitude())
//            navigateCamera(map, cameraPosition, 17.0)
//          }
//        }
//      }
//    }
//  }
//
//  private fun displayMetrics() {
//    lifecycleScope.launchWhenCreated {
//      provider.provideRecords().collect { records ->
//        val locations = records.map { it.location }
//        val metrics = MetricsManager.getLiveMetrics(locations)
//        Timber.d("Metrics: $metrics")
//        binding.metrics = metrics
//      }
//    }
//  }

//  // Used to zoom into the users location only once
//  private val isZoomedIn = AtomicBoolean(true)
//  private val positionChangeListener = OnIndicatorPositionChangedListener { point ->
//    zoomOnLocation(point, isZoomedIn.getAndSet(false))
//  }
//
//  private val locationRequestContract = registerForActivityResult(RequestPermission()) { granted ->
//    if (granted) {
//      val style = mapboxMap.style
//      if (style != null) {
//        enableLocationComponent(requireContext(), style)
//      }
//    }
//  }
//
//  @SuppressLint("MissingPermission")
//  private fun enableLocationComponent(
//    context: Context,
//    loadedMapStyle: Style,
//  ) {
//    if (PermissionsManager.isForegroundLocationPermissionGranted(requireContext())) {
//      locationComponent = mapboxMap.locationComponent
//      val locationComponentOptions = LocationComponentOptions.builder(context)
//        .pulseEnabled(true)
//        .pulseColor(ContextCompat.getColor(context, R.color.color_primary))
//        .build()
//
//      // Activate with options
//      locationComponent.activateLocationComponent(
//        LocationComponentActivationOptions.builder(context, loadedMapStyle)
//          .locationComponentOptions(locationComponentOptions)
//          .build()
//      )
//
//      // Set the component's camera mode
//      locationComponent.cameraMode = CameraMode.TRACKING_GPS
//
//      // Set the component's render mode
//      locationComponent.renderMode = RenderMode.COMPASS
//
//      // Enable to make component visible
//      locationComponent.isLocationComponentEnabled = true
//
//    } else {
//
//    }
//  }

//  private fun zoomOnLocation(point: Point, zoomIn: Boolean) {
//    if (zoomIn) {
//      navigateCamera(mapboxMap, LatLng(point.latitude(), point.longitude()))
//    }
//  }

//  private fun navigateCamera(map: MapboxMap, latLng: LatLng, zoom: Double = 15.0) {
//    val position = CameraPosition.Builder()
//      .target(latLng) // Sets the new camera position
//      .zoom(zoom) // Sets the zoom
//      .build() // Creates a CameraPosition from the builder
//    map.easeCamera(CameraUpdateFactory.newCameraPosition(position))
//  }

//  override fun onStart() {
//    super.onStart()
//    binding.mapView.onStart()
//  }
//
//  override fun onResume() {
//    super.onResume()
//    binding.mapView.onResume()
//  }
//
//  override fun onPause() {
//    super.onPause()
//    binding.mapView.onPause()
//  }
//
//  override fun onStop() {
//    super.onStop()
//    binding.mapView.onStop()
//  }
//
//  override fun onLowMemory() {
//    super.onLowMemory()
//    binding.mapView.onLowMemory()
//  }
//
//  override fun onDestroyView() {
//    super.onDestroyView()
//    binding.mapView.onDestroy()
//    locationComponent.removeOnIndicatorPositionChangedListener(positionChangeListener)
//  }
}
