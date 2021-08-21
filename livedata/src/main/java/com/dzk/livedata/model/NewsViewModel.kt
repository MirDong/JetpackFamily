package com.dzk.livedata.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 横竖屏切换，并不会改变数据
 */
class NewsViewModel: ViewModel() {
    var count = 0
    // livedata既是观察者(观察Activity生命周期), 又是被观察者(livedata数据变化时，通知其他观察者)
    private val nameLiveData = MutableLiveData<String>()

    fun getNameLiveData() = nameLiveData
}