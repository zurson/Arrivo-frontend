package com.thesis.arrivo.utilities.navigation_api

import android.os.Handler
import android.util.Log
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.google.android.libraries.navigation.NavigationApi
import com.google.android.libraries.navigation.Navigator
import com.google.android.libraries.navigation.RoutingOptions
import com.google.android.libraries.navigation.SupportNavigationFragment
import com.google.android.libraries.navigation.Waypoint
import com.thesis.arrivo.R
import com.thesis.arrivo.utilities.Settings.Companion.NAVIGATION_API_INIT_ERROR_MESSAGE
import com.thesis.arrivo.utilities.Settings.Companion.NAVIGATION_API_NOT_INITIALIZED_MESSAGE
import com.thesis.arrivo.utilities.location.Location

object NavigationApiManager {

    private var mNavigator: Navigator? = null
    private var activity: FragmentActivity? = null
    private var mNavFragment: SupportNavigationFragment? = null

    private var noRouteReload = 0
    private var noRouteReloadLimit = 3


    fun initialize(activity: FragmentActivity) {
        this.activity = activity

        NavigationApi.getNavigator(activity, object : NavigationApi.NavigatorListener {
            override fun onNavigatorReady(navigatorInstance: Navigator) {
                mNavigator = navigatorInstance
            }

            override fun onError(errorCode: Int) {
                Log.e("Navigation API", "Initialize error: $errorCode")
            }
        })
    }


    fun startNavigation(
        location: Location,
        navigationStartStatus: (Boolean, Navigator.RouteStatus) -> Unit
    ) {
        validate()

        val destination = createWaypoint(location)
        val travelMode = createRoutingOptions()

        val pendingRoute = mNavigator?.setDestination(destination, travelMode)
        pendingRoute?.setOnResultListener { routeStatus ->
            handleRouteResult(routeStatus, location, navigationStartStatus)
        }

    }


    private fun createWaypoint(location: Location): Waypoint {
        return Waypoint.builder().setLatLng(location.latitude, location.longitude).build()
    }


    private fun createRoutingOptions(): RoutingOptions {
        return RoutingOptions().apply { travelMode(RoutingOptions.TravelMode.DRIVING) }
    }


    private fun handleRouteResult(
        routeStatus: Navigator.RouteStatus,
        location: Location,
        navigationStartStatus: (Boolean, Navigator.RouteStatus) -> Unit
    ) {
        when (routeStatus) {
            Navigator.RouteStatus.OK -> handleSuccessfulRoute(navigationStartStatus, routeStatus)
            Navigator.RouteStatus.NO_ROUTE_FOUND -> handleNoRouteFound(
                location,
                navigationStartStatus
            )

            else -> handleRouteError(routeStatus, navigationStartStatus)
        }
    }


    private fun handleSuccessfulRoute(
        navigationStartStatus: (Boolean, Navigator.RouteStatus) -> Unit,
        routeStatus: Navigator.RouteStatus
    ) {
        mNavigator?.apply {
            setAudioGuidance(Navigator.AudioGuidance.VOICE_ALERTS_AND_GUIDANCE)
            startGuidance()
        }
        resetNoRouteReloadCounter()
        navigationStartStatus(true, routeStatus)
    }


    private fun handleNoRouteFound(
        location: Location,
        navigationStartStatus: (Boolean, Navigator.RouteStatus) -> Unit
    ) {
        if (noRouteReload >= noRouteReloadLimit) {
            navigationStartStatus(false, Navigator.RouteStatus.NO_ROUTE_FOUND)
            resetNoRouteReloadCounter()
        } else {
            retryNavigation(location, navigationStartStatus)
        }
    }


    private fun retryNavigation(
        location: Location,
        navigationStartStatus: (Boolean, Navigator.RouteStatus) -> Unit
    ) {
        noRouteReload++
        activity?.let { activity ->
            Handler(activity.mainLooper).postDelayed({
                startNavigation(location, navigationStartStatus)
            }, 1_000)
        }
    }


    private fun handleRouteError(
        routeStatus: Navigator.RouteStatus,
        navigationStartStatus: (Boolean, Navigator.RouteStatus) -> Unit
    ) {
        Log.e("Navigation API", routeStatus.name)
        navigationStartStatus(false, routeStatus)
    }


    private fun resetNoRouteReloadCounter() {
        noRouteReload = 0
    }


    fun stopNavigation() {
        mNavigator?.stopGuidance()
    }


    private fun validate() {
        if (activity == null)
            throw NavigationApiException(NAVIGATION_API_NOT_INITIALIZED_MESSAGE)

        if (mNavigator == null)
            throw NavigationApiException(NAVIGATION_API_INIT_ERROR_MESSAGE)
    }


    private fun setNavFragmentOptions() {
        mNavFragment?.let {
            it.setSpeedometerEnabled(true)
            it.setHeaderEnabled(true)
        }
    }


    @Composable
    fun NavigationView(modifier: Modifier = Modifier) {
        AndroidView(
            factory = { ctx ->
                FrameLayout(ctx).apply {
                    id = R.id.navigation_fragment
                }
            },
            modifier = modifier
        )

        LaunchedEffect(Unit) {
            mNavFragment = SupportNavigationFragment.newInstance()

            activity?.supportFragmentManager?.commit {
                replace(R.id.navigation_fragment, mNavFragment!!)
            }

            setNavFragmentOptions()
        }
    }
}

