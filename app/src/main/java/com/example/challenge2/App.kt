package com.example.challenge2

import android.app.Application

class App : Application() {
   override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)
    }
}