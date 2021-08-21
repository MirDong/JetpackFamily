package com.dzk.jetpackfamily.activity.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        supportActionBar?.hide()
        init()
    }

    abstract fun getLayoutId(): Int

    abstract fun init()
}