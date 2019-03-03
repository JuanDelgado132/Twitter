package com.tweet.tweeter.fragments

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.tweet.tweeter.R
import com.tweet.tweeter.controllers.MainActivity
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LoginFragment : Fragment() {
    private lateinit var emailField: EditText
    private lateinit var passField: EditText
//    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.login_fragment, container, false)

        //Get the views we need.
        emailField = view.findViewById(R.id.loginEmail)
        passField = view.findViewById(R.id.loginPasswordField)
        setupListeners(view)
        return view

    }




    private fun setupListeners(view: View){
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val email = emailField.text
            val password = passField.text

            val  payload = JSONObject()
            payload.put("loginName", email)
            payload.put("password", password)

            val payloadBytes = payload.toString().toByteArray()

            val loginTask = object : AsyncTask<Void, Void, Unit>(){
                override fun doInBackground(vararg params: Void?) {

                }

            }

        }
        val signupView = view.findViewById<TextView>(R.id.signupView)
        signupView.setOnClickListener {
            (activity as MainActivity).launchSignupFragment()
        }
    }

}
