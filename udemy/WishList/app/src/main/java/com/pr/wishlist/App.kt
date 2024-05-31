package com.pr.wishlist

import android.app.Application

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}

