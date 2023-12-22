package com.example.barbuddy

import android.app.Application


object DatabaseProvider {
    lateinit var db: MyAppDatabase
}
class RoomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseProvider.db = MyAppDatabase.create(applicationContext)
    }
}