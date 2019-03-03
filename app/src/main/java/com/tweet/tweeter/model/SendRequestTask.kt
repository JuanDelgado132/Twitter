package com.tweet.tweeter.model


import android.os.AsyncTask
import android.util.Log
import com.tweet.tweeter.controllers.App
import com.tweet.tweeter.interfaces.RequestTaskListener
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class SendRequestTask(private val requestData: ByteArray?) : AsyncTask<URL, String, String>(){
    //Listener to update the main ui
    var requestListener: RequestTaskListener? = null

    override fun doInBackground(vararg params: URL?): String {
        //Pass the url parameter
        val url = params[0]
        //Will hold the length of the payload.
        lateinit var contentLength: String
        //Result of the httprequest
        lateinit var result: String
        //Decide length of payload.
        if (requestData != null)
            contentLength = requestData.size.toString()
        else
            contentLength = "0"
        //If url is not null we do the operations.
        if(url !== null) {
            val connection = url.openConnection() as HttpsURLConnection
            //Set our connection properties
            connection.requestMethod = "POST"
            connection.connectTimeout = 3000000
            connection.doOutput = true
            connection.doInput = true
            connection.setRequestProperty("charset", "utf-8")
            connection.setRequestProperty("Content-length", contentLength)
            connection.setRequestProperty("Content-type", "application/json")
            connection.connect()

            try {
                val outputStream = DataOutputStream(connection.outputStream)
                outputStream.write(requestData)
                outputStream.flush()

            } catch (e: Exception) {

            }
            if (connection.responseCode != HttpsURLConnection.HTTP_OK && connection.responseCode != HttpsURLConnection.HTTP_CREATED) {

            } else {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream, "utf-8"))

                result = reader.readText()
                Log.d("Response", result);
//                val responseObject = JSONObject(result)
//
//                val user = User(responseObject.getInt("id"),responseObject.getString("email"), responseObject.getString("firstName")
//                    , responseObject.getString("lastName"), responseObject.getString("username"), responseObject.getString("password")
//                    , responseObject.getString("profilePicture"))
//                App.user = user



            }
            return  result
        } else {
            return "ERROR"
        }

    }
    //Once signup has been successful. We will launch to the twitter feed.

    override fun onPostExecute(result: String?) {
        if (requestListener != null){
            Log.d("CALLING MY LISTENER", "I AM CALLING")
            requestListener!!.onRequestFinished(result)

        }

        super.onPostExecute(result)
    }
}