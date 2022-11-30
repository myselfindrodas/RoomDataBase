package com.example.weatherlogger.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherlogger.MainActivity
import com.example.weatherlogger.R
import com.example.weatherlogger.model.WeatherModel
import com.example.weatherlogger.roomdatabase.Weather
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeatherAdapter(
    ctx: MainActivity,
    weatherModelArrayList: MutableList<Weather?>?,
) :
    RecyclerView.Adapter<WeatherAdapter.MyViewHolder>() {
    private val inflater: LayoutInflater
    private val weatherModelArrayList: MutableList<Weather?>?
    var ctx: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = inflater.inflate(R.layout.rv_weather, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate: String = sdf.format(Date())

        holder.tvTemp.text = "Temperature : "+ weatherModelArrayList!![position]!!.temp
        holder.tvDate.text = "Date : "+ currentDate

    }

    override fun getItemCount(): Int {
        return weatherModelArrayList!!.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTemp: TextView
        var tvDate: TextView


        init {
            tvTemp = itemView.findViewById(R.id.tvTemp)
            tvDate = itemView.findViewById(R.id.tvDate)
        }
    }

    init {
        inflater = LayoutInflater.from(ctx)
        this.weatherModelArrayList = weatherModelArrayList
        this.ctx = ctx
    }
}