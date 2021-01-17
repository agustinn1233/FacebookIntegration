package com.agustinf1233.facebookintegration

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton


class MainActivity : AppCompatActivity() {

    private var callbackManager:CallbackManager? = null
    private var profileTracker:ProfileTracker? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callbackManager = CallbackManager.Factory.create()
        val tvData = findViewById<TextView>(R.id.tvData)
        val btnLogin = findViewById<LoginButton>(R.id.btnLogin)
        btnLogin.setReadPermissions("email")


        if(AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
            tvData.text = Profile.getCurrentProfile().firstName + " " + Profile.getCurrentProfile().lastName
        }

        // Callback registration
        btnLogin.registerCallback(callbackManager, object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {

                val accessToken = AccessToken.getCurrentAccessToken()
                val isLoggedIn = accessToken != null && !accessToken.isExpired

                Log.d("ACCESS_TOKEN", accessToken.token)

                if(Profile.getCurrentProfile() == null) {
                    profileTracker = object: ProfileTracker() {
                        override fun onCurrentProfileChanged(
                            oldProfile: Profile?,
                            currentProfile: Profile?
                        ) {
                            if (currentProfile != null) {
                                Log.d("NOMBRE", currentProfile.name)
                            }
                        }

                    }
                } else {
                    val profile = Profile.getCurrentProfile()
                    profile?.firstName?.let { Log.d("NOMBRE", it) }
                    tvData.text = profile?.firstName + " " + profile?.lastName
                    profileTracker?.stopTracking()

                }
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onError(error: FacebookException?) {
                TODO("Not yet implemented")
            }

        })

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    // App code
                }

                override fun onCancel() {
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    // App code
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
    }
}