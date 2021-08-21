package com.dzk.jetpackfamily.activity

import com.dzk.jetpackfamily.R
import com.dzk.jetpackfamily.activity.base.BaseActivity
import com.dzk.jetpackfamily.presenter.NewsPresenter

/**
 * 事实上， MainActivity的父类ComponentActivity已经实现了LifecycleOwner
 */
class MainActivity : BaseActivity() {
    private val newsPresenter: NewsPresenter = NewsPresenter()
    override fun getLayoutId(): Int = R.layout.activity_main

    override fun init() {
        lifecycle.addObserver(newsPresenter)
    }
}