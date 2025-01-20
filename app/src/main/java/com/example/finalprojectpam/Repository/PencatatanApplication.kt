package com.example.finalprojectpam.Repository

import android.app.Application

class PencatatanApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = PencatatanKeuangan()
    }
}