package com.example.challenge2

import android.content.Context
import io.objectbox.BoxStore

object ObjectBox {
    private var boxStore: BoxStore? = null
    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
    }

    fun get(): BoxStore? {
        return boxStore
    }
}