package com.dzk.livedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dzk.livedata.bus.LiveDataBus

class LiveDataBusTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data_bus_test)
        LiveDataBus.with(DATA, String::class.java)?.observe(this) { data ->
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
        }
    }
}