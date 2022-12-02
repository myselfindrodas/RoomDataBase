package com.demo.weatherlogger

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.hcsassist.retrofit.ApiClient
import com.example.weatherlogger.internet.CheckConnectivity
import com.example.weatherlogger.modelfactory.WeathermodelFactory
import com.example.weatherlogger.retrofit.ApiHelper
import com.example.weatherlogger.utils.Status
import com.example.weatherlogger.viewmodel.WeatherViewModel
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), NoteClickInterface, NoteClickDeleteInterface {

    lateinit var viewModal: NoteViewModal
    private lateinit var viewModel: WeatherViewModel
    lateinit var notesRV: RecyclerView
    var locationRequest: LocationRequest? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var locationCallback: LocationCallback?=null
    var locationManager: LocationManager? = null
    var REQUEST_CODE = 101



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notesRV = findViewById(R.id.notesRV)

        notesRV.layoutManager = LinearLayoutManager(this)
        val noteRVAdapter = NoteRVAdapter(this, this, this)
        notesRV.adapter = noteRVAdapter
        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModal::class.java)

        val vm: WeatherViewModel by viewModels {
            WeathermodelFactory(ApiHelper(ApiClient.apiService))
        }
        viewModel = vm

        requestAllPermission()


        viewModal.allNotes.observe(this, Observer { list ->
            list?.let {
                noteRVAdapter.updateList(it)
            }
        })

    }

    private fun getLocation() {


        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
        } else {

            val secondsDelayed = 1
            Handler().postDelayed({
                setLocationListner()
            }, (secondsDelayed * 1000).toLong())


        }
    }



    private fun setLocationListner() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(60000).setFastestInterval(60000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        locationCallback=object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    if (location.latitude != null && location.longitude != null) {
                    Log.d(TAG, "lat long-->"+locationResult.locations[0].latitude+","+locationResult.locations[0].longitude)

                            currentWeather(location.latitude.toString(), location.longitude.toString())

                    }
                }

            }
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }




    private fun requestAllPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        Toast.makeText(applicationContext, "All permissions are granted!", Toast.LENGTH_SHORT).show()

                        getLocation()


                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(
                    applicationContext,
                    "Error occurred! ",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .onSameThread()
            .check()
    }


    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings and reopen the application.")
        builder.setPositiveButton(
            "GOTO SETTINGS"
        ) { dialog, which ->
            dialog.cancel()
            finishAffinity()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }
        builder.show()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun currentWeather(lat:String, lon:String) {

        if (locationCallback!=null){
            fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
            locationCallback==null
        }
        if (CheckConnectivity.getInstance(this).isOnline) {
            viewModel.weather(lat =lat, lon = lon, appid = "c9eacc120b3582f2901a18cd42ba8341")
                .observe(this) {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                hideProgressDialog()
                                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                                val currentDate: String = sdf.format(Date())

                                viewModal.deleteAllNote()
                                viewModal.addNote(Weather(resource.data?.data?.temp!!, currentDate))

                            }
                            Status.ERROR -> {
                                hideProgressDialog()
                                val builder = AlertDialog.Builder(this)
                                builder.setMessage(it.message)
                                builder.setPositiveButton(
                                    "Ok"
                                ) { dialog, which ->

                                    dialog.cancel()

                                }
                                val alert = builder.create()
                                alert.show()
                            }

                            Status.LOADING -> {
                                showProgressDialog()
                            }

                        }

                    }
                }

        }else{


            val builder = AlertDialog.Builder(this)
            builder.setMessage("Ooops! Internet Connection Error")
            builder.setPositiveButton(
                "Ok"
            ) { dialog, which ->

                dialog.cancel()

            }
            val alert = builder.create()
            alert.show()
        }

    }




    var mProgressDialog: ProgressDialog? = null

    fun showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog!!.setMessage("Loading...")
            mProgressDialog!!.isIndeterminate = true
        }
        mProgressDialog!!.show()
    }

    fun hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }



    override fun onNoteClick(weather: Weather) {

    }

    override fun onDeleteIconClick(weather: Weather) {

    }


    override fun onPause() {
        super.onPause()
        if (locationCallback!=null){
            fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
            locationCallback==null

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (locationCallback!=null){
            fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
            locationCallback==null

        }
    }

}