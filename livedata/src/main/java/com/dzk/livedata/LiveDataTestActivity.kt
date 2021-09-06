package com.dzk.livedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.MutableLiveData

class LiveDataTestActivity : AppCompatActivity() {
    private val TAG = "LiveDataTestActivity"
    private val tvName: TextView by lazy { findViewById<TextView>(R.id.tvName) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data_test)
        val liveData = MutableLiveData<String>()
        tvName.setOnClickListener {
            liveData.value = "1"
        }
        liveData.observe(this, {
            Log.d(TAG, "onChange1->$it")
            if (it == "1") {
                liveData.value = "2"
            }
        })

        liveData.observe(this, {
            Log.d(TAG, "onChange2->$it")
            tvName.text = "${tvName.text}$it"
        })
    }
}