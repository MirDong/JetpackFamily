package com.dzk.livedata

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dzk.livedata.bus.LiveDataBus
import com.dzk.livedata.bus.LiveDataBusX
import com.dzk.livedata.model.NewsViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var btLiveDataBusX: Button
    private lateinit var tvName: TextView
    private lateinit var btStart: Button
    private lateinit var btLiveData: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btLiveDataBusX = findViewById(R.id.btLiveDataBusX)
        tvName = findViewById(R.id.tvName)
        btStart = findViewById(R.id.btStart)
        btLiveData = findViewById(R.id.btLiveData)

        viewModel = ViewModelProvider(
            viewModelStore,
            defaultViewModelProviderFactory
        ).get(NewsViewModel::class.java)

        viewModel.getNameLiveData().observe(this, { content ->
            tvName.text = content
        })

        tvName.setOnClickListener {
            viewModel.getNameLiveData().value = "Hello World ${++viewModel.count}"
        }

        btStart.setOnClickListener {
            LiveDataBus.with(DATA, String::class.java)?.postValue("123456")
            startActivity(Intent(this, LiveDataBusTestActivity::class.java))
        }

        btLiveData.setOnClickListener {
            startActivity(Intent(this, LiveDataTestActivity::class.java))
        }

        btLiveDataBusX.setOnClickListener {
            LiveDataBusX.with(INFO, String::class.java)?.postValue("今天天气真好")
            startActivity(Intent(this, LiveDataBusXTestActivity::class.java))
        }
    }
}