package com.example.wheather

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Math.ceil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lat=intent.getStringExtra("lat")
        val long =intent.getStringExtra("long")
//        Toast.makeText(this,"$lat & $long",Toast.LENGTH_LONG).show()
        window.statusBarColor= Color.parseColor("#1383C3")
        getJson(lat,long)
    }

    private fun getJson(lat: String?, long: String?) {
        val api=""
        val queue = Volley.newRequestQueue(this)
        val url ="https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${api}"


        val jsonRequest =JsonObjectRequest(
            Request.Method.GET,url,null,
            Response.Listener {response ->
                setjson(response)
//                Toast.makeText(this,"$response",Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { Toast.makeText(this,"Error",Toast.LENGTH_LONG).show() })



        queue.add(jsonRequest)
    }

    private fun setjson(response: JSONObject) {
        city_name.text=response.getString("name")
        val lat=response.getJSONObject("coord").getString("lat")
        val lon=response.getJSONObject("coord").getString("lon")
        cordinates.text="$lat , $lon"
        wheather.text=response.getJSONArray("weather").getJSONObject(0).getString("main")
        var tempr=response.getJSONObject("main").getString("temp")
        tempr=((tempr.toFloat()-273.15).toInt()).toString()
        temp.text="$tempr °C"
        var mintempr=response.getJSONObject("main").getString("temp_min")
        mintempr=((mintempr.toFloat()-273.15).toInt()).toString()
        min_temp.text="min:- $mintempr °C"
        var maxtempr=response.getJSONObject("main").getString("temp_max")
        maxtempr=((ceil(maxtempr.toFloat()-273.15)).toInt()).toString()
        max_temp.text="max:- $maxtempr °C"
        pressure.text=response.getJSONObject("main").getString("pressure")
        val humi=response.getJSONObject("main").getString("humidity")
        humidity.text="$humi %"
        wind.text=response.getJSONObject("wind").getString("speed")
        val d=response.getJSONObject("wind").getString("deg")
        deg.text="Deg:- $d °"


    }
}