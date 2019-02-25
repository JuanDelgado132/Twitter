package com.tweet.tweeter.controllers

import android.app.Application
import com.tweet.tweeter.model.Preferences
import com.tweet.tweeter.model.User

class App : Application(){

    companion object {
        private lateinit var prefs: Preferences
        val preferences: Preferences
            get() = prefs
         var user: User? = null
    }

    override fun onCreate() {
        prefs = Preferences(applicationContext)
        super.onCreate()

    }
}