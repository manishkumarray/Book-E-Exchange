package com.example.bookexchange

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.provider.Settings
import com.google.android.gms.location.FusedLocationProviderClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {

//  Variables
    private val SPLASH_TIME_OUT:Long = 1000
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null
    private var latitudeLabel: String? = null
    private var longitudeLabel: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Get location
        latitudeLabel = resources.getString(R.string.latitudeBabel)
        longitudeLabel = resources.getString(R.string.longitudeBabel)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        }
        public override fun onStart() {
            super.onStart()


            if (!checkPermissions()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions()
                }
            }
            else {
                getLastLocation()

            }
        }
        private fun getLastLocation() {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    lastLocation = task.result

//                    splash screen
                    Handler().postDelayed({
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        val intent=Intent(this,login::class.java)
                        intent.putExtra("latitude",(lastLocation)!!.latitude.toString())
                        intent.putExtra("longitude",(lastLocation)!!.longitude.toString())
                        startActivity(intent)
                        finish()
                    }, SPLASH_TIME_OUT)
                }
                else {
                    Log.w(TAG, "getLastLocation:exception", task.exception)
                    showMessage("No location detected. Make sure location is enabled on the device.")
                }
            }
        }
        private fun showMessage(string: String) {
            val container = findViewById<View>(R.id.cLayout)
            if (container != null) {
                Toast.makeText(this@MainActivity, string, Toast.LENGTH_LONG).show()
            }
        }
        private fun showSnackbar(
            mainTextStringId: String, actionStringId: String,
            listener: View.OnClickListener
        ) {
            Toast.makeText(this@MainActivity, mainTextStringId, Toast.LENGTH_LONG).show()
        }
        private fun checkPermissions(): Boolean {
            val permissionState = ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            return permissionState == PackageManager.PERMISSION_GRANTED
        }
        private fun startLocationPermissionRequest() {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
        private fun requestPermissions() {
            val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (shouldProvideRationale) {
                Log.i(TAG, "Displaying permission rationale to provide additional context.")
                showSnackbar("Location permission is needed for core functionality", "Okay",
                    View.OnClickListener {
                        startLocationPermissionRequest()
                    })
            }
            else {
                Log.i(TAG, "Requesting permission")
                startLocationPermissionRequest()
            }
        }
        override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>,
            grantResults: IntArray
        ) {
            Log.i(TAG, "onRequestPermissionResult")
            if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
                when {
                    grantResults.isEmpty() -> {
                        // If user interaction was interrupted, the permission request is cancelled and you
                        // receive empty arrays.
                        Log.i(TAG, "User interaction was cancelled.")
                    }
                    grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                        // Permission granted.
                        getLastLocation()
                    }
                    else -> {
                        showSnackbar("Permission was denied", "Settings",
                            View.OnClickListener {
                                // Build intent that displays the App settings screen.
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                val uri = Uri.fromParts(
                                    "package",
                                    Build.DISPLAY, null
                                )
                                intent.data = uri
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }
                        )
                    }
                }
            }

        }

        companion object {
            private val TAG = "LocationProvider"
            private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
        }
}