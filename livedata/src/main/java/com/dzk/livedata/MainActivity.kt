package com.dzk.livedata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dzk.livedata.bus.LiveDataBus
import com.dzk.livedata.model.NewsViewModel
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var tvName: TextView
    private lateinit var btStart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvName = findViewById(R.id.tvName)
        btStart = findViewById(R.id.btStart)

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
            thread {
                repeat(20) { data ->
                    Thread.sleep(1000)
                    LiveDataBus.with(DATA, String::class.java)?.postValue("$data")
                }
            }
            startActivity(Intent(this, LiveDataBusTestActivity::class.java))
        }
    }
}