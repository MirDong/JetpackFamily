package com.dzk.jetpackfamily.presenter

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.dzk.jetpackfamily.constants.MODULE_TAG
import com.dzk.jetpackfamily.presenter.base.BasePresenter

class NewsPresenter: BasePresenter() {
     val NEWS_TAG: String = MODULE_TAG + javaClass.canonicalName
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun onCreate() {
        Log.d(NEWS_TAG, "onCreate: ")
    }
}