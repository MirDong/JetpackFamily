package com.dzk.livedata.bus

import androidx.lifecycle.MutableLiveData

object LiveDataBus {
    private val mLiveDataMap = HashMap<String, MutableLiveData<Any>>()

    fun <T> with(key: String, clazz: Class<T>): MutableLiveData<T>? {
        return mLiveDataMap.getOrPut(key, { MutableLiveData<Any>() }) as? MutableLiveData<T>
    }
}