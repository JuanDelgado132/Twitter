package com.tweet.tweeter.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.tweet.tweeter.R
import com.tweet.tweeter.controllers.App
import com.tweet.tweeter.controllers.MainActivity
import com.tweet.tweeter.controllers.TwitterFeed
import com.tweet.tweeter.interfaces.RequestTaskListener
import com.tweet.tweeter.model.Preferences
import com.tweet.tweeter.model.SendRequestTask
import com.tweet.tweeter.model.User
import com.tweet.tweeter.utils.Constants
import kotlinx.android.synthetic.main.fragment_signup.*
import org.json.JSONObject
import java.io.*
import java.net.URL
import java.nio.charset.Charset


class SignupFragment : Fragment(), RequestTaskListener{


    private lateinit var  emailField: EditText
    private lateinit var  passwordField: EditText
    private lateinit var  firstNameField: EditText
    private lateinit var  lastNameField: EditText
    private lateinit var  usernameField: EditText
    private lateinit var  pictureField: ImageView
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        //Get the views we need.
        emailField = view.findViewById(R.id.signupEmail)
        passwordField = view.findViewById(R.id.signupPassword)
        firstNameField = view.findViewById(R.id.signupFirstName)
        lastNameField = view.findViewById(R.id.signupLastname)
        usernameField = view.findViewById(R.id.signupUsername)
        pictureField = view.findViewById(R.id.profilePic)
        setupListeners(view)
        //Lets re-add the picture.
        if(App.preferences.isProfilePicSet){
           val imageString = App.preferences.picture
            val imageBytes = Base64.decode(imageString, Base64.DEFAULT)

           pictureField.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size))
        }
        return view

    }


    override fun onDetach() {
        super.onDetach()
       // listener = null
    }
    private fun setupListeners(view: View){
        val signupButton = view.findViewById<Button>(R.id.signupBtn)
        signupButton.setOnClickListener {
            enableSpinner(true)
            var email = emailField.text
            var password = passwordField.text
            var firstName = firstNameField.text
            var lastName = lastNameField.text
            var username = usernameField.text
            if (email.isNotEmpty() && password.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty() && username.isNotEmpty()){
                //Convert image to byte array.
                val image = (pictureField.drawable as BitmapDrawable).bitmap
                val stream = ByteArrayOutputStream()
                image.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val imageBytes = stream.toByteArray()
                //Encode the image bytes into a base64 string, so that we can attach it to a JSON object.
                val imageString = Base64.encodeToString(imageBytes, 0)

                //Construct our JSON object
                val payload = JSONObject()
                payload.put("email", email)
                payload.put("password", password)
                payload.put("First Name", firstName)
                payload.put("Last Name", lastName)
                payload.put("Username", username)
                payload.put ("Profile Picture", imageString)
                //turn our payload into a byte array so that it could be sent.
                val postData = payload.toString().toByteArray()
                //Create our url
                val url = URL(Constants.REGISTER_URL)
                //Initialize the asynctask, so that it can run in the background.
                val task = SendRequestTask(postData)

                task.execute(url)

            }
            else {
                enableSpinner(false)
                Toast.makeText(activity, "Please enter the information for all fields.", Toast.LENGTH_LONG).show()
            }


        }
        pictureField.setOnClickListener {

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PICK_IMAGE_CODE)
        }
    }

    /**
     * Override as user will select pictures from their phone to upload as profile pic.
     *
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Constants.PICK_IMAGE_CODE){
            if(data != null){


                //Pass the string representation of the URI to the addImageToProfileView function.
                addImageToProfileView(data.dataString)
                Log.d("IMAGE", data.dataString)
            }
        }
    }
    private fun addImageToProfileView(imageUriString: String){
        val image = Uri.parse(imageUriString)
        val imageStream = (activity as MainActivity).contentResolver.openInputStream(image)
        pictureField.setImageBitmap(BitmapFactory.decodeStream(imageStream))
        imageStream.close()

        //Save the image uri and set the picture boolean to true in the preferences class.
        App.preferences.isProfilePicSet = true
        App.preferences.picture = convertImageToBase64String((pictureField.drawable as BitmapDrawable).bitmap)
    }


     private fun enableSpinner(enable: Boolean){
        if(enable)
            signupSpinner.visibility = View.VISIBLE
        else
            signupSpinner.visibility = View.INVISIBLE
        emailField.isEnabled = !enable
        passwordField.isEnabled = !enable
        firstNameField.isEnabled = !enable
        lastNameField.isEnabled = !enable
        usernameField.isEnabled = !enable
        pictureField.isEnabled = !enable

    }
    private fun convertImageToBase64String(imageBitmap: Bitmap): String{
        val outputStream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val imageBytes = outputStream.toByteArray()
        return Base64.encodeToString(imageBytes, 0)
    }

    /**
     * @author Juan Delgado
     * @param result
     * Takes the JSON response and parses it into a user variable that is then given to the User global object.
     *
     */
    override fun onRequestFinished(result: String?) {
        //Check that result is not null.
        if(result != null){
            //Parse the string into a JSONObject
            val responseObject = JSONObject(result)
            //Extract the data.
            val user = User(responseObject.getInt("id"),responseObject.getString("email"), responseObject.getString("firstName")
                , responseObject.getString("lastName"), responseObject.getString("username"), responseObject.getString("password")
                , responseObject.getString("profilePicture"))
            //Save it
            App.user = user
        }

        enableSpinner(false)
        if(activity != null) {
            val twitterFeedIntent = Intent(activity, TwitterFeed::class.java)
            activity!!.startActivity(twitterFeedIntent)
            activity!!.finish()
        }
        else {
            Toast.makeText(activity, "Something went wrong, please try again!", Toast.LENGTH_LONG).show()
        }

    }
}