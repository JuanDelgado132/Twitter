package com.tweet.tweeter.controllers


import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import com.tweet.tweeter.R
import com.tweet.tweeter.fragments.LoginFragment
import com.tweet.tweeter.fragments.SignupFragment
import com.tweet.tweeter.interfaces.RequestTaskListener
import com.tweet.tweeter.utils.Constants
import kotlin.math.sign

class MainActivity : AppCompatActivity(){

    //My fragment manager and transaction
    private lateinit var  fragManager: FragmentManager
    private lateinit var fragTransaction: FragmentTransaction
    //Login fragment
    private lateinit var  loginFragment: LoginFragment
    //Signup fragment
    private lateinit var  signupFragment: SignupFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null) {
            fragManager = supportFragmentManager
           // fragTransaction = fragManager.beginTransaction()
            loginFragment = LoginFragment()
            signupFragment = SignupFragment()
           // fragTransaction.add(R.id.tweeterContainer, signupFragment, Constants.SIGN_UP_TAG)
           // fragTransaction.commit()
            launchSignupFragment()

        }

    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun launchSignupFragment(){
        fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.tweeterContainer, signupFragment, Constants.SIGN_UP_TAG)
        fragTransaction.commit()
    }
    fun launchLoginFragment(){
        fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.tweeterContainer, loginFragment, Constants.LOGIN_IN_TAG)
        fragTransaction.commit()

    }


    enum class FRAGMENT{
        LOGIN, SIGNUP
    }

}
