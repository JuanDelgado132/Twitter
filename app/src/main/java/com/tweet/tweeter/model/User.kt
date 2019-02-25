package com.tweet.tweeter.model
import kotlinx.android.parcel.Parcelize

import android.os.Parcelable

@Parcelize
data class User (private var id: Int, private var email: String, private var firstName: String, private var lastName: String,
                 private var userName: String, private var password: String, private var picture: String?) : Parcelable {
    var userId: Int
        get() = id
        set(value){ id = value }
    var userEmail: String
        get() = email
        set(value) {email = value}
    var userFirstName: String
        get() = firstName
        set(value){firstName = value}
    var userLastName: String
        get() = lastName
        set(value) {lastName = value}
    var userUserName: String
        get() = userName
        set(value) {userName = value}
    var userPassword: String
        get() = password
        set(value){password = value}
    var userPicture: String?
        get() = picture
        set(value) {picture = value}
}