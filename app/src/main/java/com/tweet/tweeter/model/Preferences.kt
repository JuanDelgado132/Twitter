package com.tweet.tweeter.model

import android.content.Context

class Preferences(context: Context) {
    val FILENAME = "tweeter-prefs"
    val prefs = context.getSharedPreferences(FILENAME, 0)
    val SIGNUP_PIC = "signupPic"
    val SIGNUP_PIC_SET = "signupPicSet"
    var isProfilePicSet: Boolean
        get() = prefs.getBoolean(SIGNUP_PIC_SET, false)
        set(value) = prefs.edit().putBoolean(SIGNUP_PIC_SET, value).apply()
    var picture : String
        get() = prefs.getString(SIGNUP_PIC, "")
        set(value) =  prefs.edit().putString(SIGNUP_PIC, value).apply()


}