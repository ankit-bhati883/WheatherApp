package com.example.wheather

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*


class SplashScreen : AppCompatActivity() {
    private lateinit var mfusedlocation:FusedLocationProviderClient
    private var myRequestcode=1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mfusedlocation=LocationServices.getFusedLocationProviderClient(this)
        getlastlocation()

    }

    // 1.location permission--> deny
    //2.location denied through settings
    //3. gps off
    //4. permission lelo
    @SuppressLint("MissingPermission")
    private fun getlastlocation() {
        if(checkpermission()){
          if(locationEnabled()){
              mfusedlocation.lastLocation.addOnCompleteListener{
                  task->
                  var location: Location?=task.result
                  if(location==null){
                      NewLocation()
                  }else{
                      Handler(Looper.getMainLooper()).postDelayed({
                          val intent= Intent(this,MainActivity::class.java)
                          intent.putExtra("lat",location.latitude.toString())
                          intent.putExtra("long",location.longitude.toString())
                           startActivity(intent)
                           finish()
                      },2000)
                  }

              }
          }else{
              Toast.makeText(this,"Please Turn on your gps location",Toast.LENGTH_LONG).show()
          }

        }else{
            RequestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun NewLocation() {

        var locationRequest= LocationRequest()
        locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval=0
        locationRequest.fastestInterval=0
        locationRequest.numUpdates=1
        mfusedlocation=LocationServices.getFusedLocationProviderClient(this)
        mfusedlocation.requestLocationUpdates(locationRequest,locationCallback,Looper.myLooper())
    }

    private val locationCallback=object : LocationCallback(){

        override fun onLocationResult(p0: LocationResult) {
            var lastlocation:Location=p0.lastLocation
        }
    }
    private fun locationEnabled(): Boolean {

        var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun RequestPermission() {
        ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION),myRequestcode)
    }

    private fun checkpermission(): Boolean {

        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if(requestCode==myRequestcode){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getlastlocation()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}