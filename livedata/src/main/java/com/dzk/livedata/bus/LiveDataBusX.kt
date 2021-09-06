package com.dzk.livedata.bus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.lang.NullPointerException
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * 去除粘性数据(即先发布数据再订阅时，也会收到未订阅前的数据)
 */
object LiveDataBusX {
    private val mLiveDataMap = HashMap<String, MutableLiveData<Any>>()

    fun <T> with(key: String, clazz: Class<T>): MutableLiveData<T>? {
        return mLiveDataMap.getOrPut(key, { MutableLiveData<Any>() }) as? MutableLiveData<T>
    }

    class UnStickMutableLiveData<T>: MutableLiveData<T>() {
        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
            hook(observer)
        }
    }

    /**
     * 通过反射使ObserverWrap.mLastVersion=LiveData.version
     */
    private fun <T> hook(observer: Observer<in T>) {
        try {// 1. 获取mLastVersion
            val liveDataClazz = LiveData::class.java
            // 获取到LiveData中的mObservers
            val observerField: Field  = liveDataClazz.getDeclaredField("mObservers")
            observerField.isAccessible = true
            // 获取mObservers这个对象
            val observersObj = observerField.get(this)

            val observerClazz = observersObj.javaClass
            // 获取mObservers对象的get方法
            val observerMethod: Method = observerClazz.getDeclaredMethod("get", Any::class.java)
            observerMethod.isAccessible = true
            val objEntry = observerMethod.invoke(observersObj, observer)
            var observerWrap: Any? = null
            if (objEntry != null && objEntry is Map.Entry<*, *>) {
                observerWrap = objEntry.value
            }
            if (observerWrap == null) throw NullPointerException("observerWrap is null")
            val observerWrapClass = observerWrap.javaClass
            val mLastVersion = observerWrapClass.getDeclaredField("mLastVersion")
            mLastVersion.isAccessible = true

            val mVersionField = liveDataClazz.getDeclaredField("mVersion")
            mVersionField.isAccessible = true
            val mVersion = mVersionField.get(this)

            mLastVersion.set(observerWrap, mVersion)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}