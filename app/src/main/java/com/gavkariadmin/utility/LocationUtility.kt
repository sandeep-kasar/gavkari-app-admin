package com.gavkariadmin.utility

import android.app.Activity
import com.gavkariadmin.interfaces.MessageResponseCallback


class LocationUtility(var activity: Activity,
                      var messageResponseCallback: MessageResponseCallback
) {

  /*  var mLocationManager: LocationManager? = null
    var mGoogleApiClient: GoogleApiClient? = null

    *//**
     * check play service
     *//*
    fun checkPlayServices(): Boolean {

        val resultCode = GoogleApiAvailability.getInstance().
                isGooglePlayServicesAvailable(activity)

        if (resultCode != ConnectionResult.SUCCESS) {
            if (GoogleApiAvailability.getInstance().isUserResolvableError(resultCode)) {
                GoogleApiAvailability.getInstance().getErrorDialog(activity, resultCode,
                        AppConstant.PLAY_SERVICES_RESOLUTION_REQUEST).show()
            } else {
                return false
            }
            return false
        }

        return true
    }

    *//**
     * check gps support
     *//*
    fun hasGPSDevice(context: Context): Boolean {
        val mgr = context
                .getSystemService(Context.LOCATION_SERVICE) as LocationManager ?: return false
        val providers = mgr.allProviders ?: return false
        return providers.contains(LocationManager.GPS_PROVIDER)
    }

    *//**
     * init location manager
     *//*
    fun initializeLocationManager() {

        if (mLocationManager == null) {
            mLocationManager = getApplicationContext().getSystemService(Context.LOCATION_SERVICE)
                    as LocationManager
        }
    }

    *//**
     * check location is on or not
     *//*
    fun checkLocation() {

        //Location Already on
        if (mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && hasGPSDevice(activity)) {

            activity.startService(Intent(activity, LocationService::class.java))

        }

        //check gps device
        if (!hasGPSDevice(activity)) {
            messageResponseCallback.returnMessage(activity.getString(R.string.err_no_gps))

        }

        //check gps in started or not
        if (!mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && hasGPSDevice(activity)) {

            enableLocation()

        }
    }

    *//**
     * enable location
     *//*
    fun enableLocation() {

        mGoogleApiClient = GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(bundle: Bundle?) {

                    }

                    override fun onConnectionSuspended(i: Int) {
                        mGoogleApiClient?.connect()
                    }
                })
                .addOnConnectionFailedListener { connectionResult ->
                    Log.d("Location error", "Location error " + connectionResult.errorCode)
                }.build()

        mGoogleApiClient?.connect()

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 30 * 1000
        locationRequest.fastestInterval = 5 * 1000
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        builder.setAlwaysShow(true)

        val result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build())
        result.setResultCallback { result ->
            val status = result.status

            when (status.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    status.startResolutionForResult(activity, LOCATION_PERMISSION_CONSTANT)
                } catch (e: IntentSender.SendIntentException) {

                }

            }
        }


    }*/


}