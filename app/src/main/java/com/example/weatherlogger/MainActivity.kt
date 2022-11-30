package com.example.weatherlogger

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.hcsassist.retrofit.ApiClient
import com.example.weatherlogger.adapter.WeatherAdapter
import com.example.weatherlogger.databinding.ActivityMainBinding
import com.example.weatherlogger.internet.CheckConnectivity
import com.example.weatherlogger.modelfactory.WeathermodelFactory
import com.example.weatherlogger.retrofit.ApiHelper
import com.example.weatherlogger.roomdatabase.Weather
import com.example.weatherlogger.roomdatabase.WeatherDatabase
import com.example.weatherlogger.utils.Status
import com.example.weatherlogger.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel
    private var list: MutableList<Weather?>? = null
    lateinit var weatherAdapter: WeatherAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val vm: WeatherViewModel by viewModels {
            WeathermodelFactory(ApiHelper(ApiClient.apiService))
        }
        viewModel = vm

        mainBinding.rvTemp.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        )


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val appDb = WeatherDatabase.getInstance(this)
        list = appDb!!.weatherDao()?.getWeatherList()
        weatherAdapter = WeatherAdapter(this, list)
        mainBinding.rvTemp.setAdapter(
            weatherAdapter
        )

        currentWeather()


    }


    @SuppressLint("NotifyDataSetChanged")
    private fun currentWeather() {

        if (CheckConnectivity.getInstance(this).isOnline) {

            viewModel.weather(q = "Kolkata", appid = "c9eacc120b3582f2901a18cd42ba8341")
                .observe(this) {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                hideProgressDialog()
                                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                                val currentDate: String = sdf.format(Date())
                                val appDb = WeatherDatabase.getInstance(this)

                                val weather = Weather(resource.data?.data?.temp!!, currentDate, 0)
                                appDb!!.weatherDao()?.insertWeather(weather)
                                list?.clear()
                                list?.addAll(appDb.weatherDao()?.getWeatherList()!!)
                                weatherAdapter.notifyDataSetChanged()

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
            Toast.makeText(this, "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show()
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
}