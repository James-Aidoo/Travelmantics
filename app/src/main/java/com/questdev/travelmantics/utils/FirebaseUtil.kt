package com.questdev.travelmantics.utils

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.questdev.travelmantics.TravelDeal

class FirebaseUtil private constructor() {

    companion object {
        private const val RC_SIGN_IN = 123
        lateinit var firebaseDatabase: FirebaseDatabase
        lateinit var databaseReference: DatabaseReference
        lateinit var firebaseAuth: FirebaseAuth
        lateinit var authListener: AuthStateListener
        lateinit var deals: ArrayList<TravelDeal>
        lateinit var caller: Activity
        var firebaseUtil: FirebaseUtil? = null

        fun openFbReference(ref: String, callerActivity: Activity) {
            if (firebaseUtil == null){
                firebaseUtil = FirebaseUtil()
                firebaseDatabase = FirebaseDatabase.getInstance()
                firebaseAuth = FirebaseAuth.getInstance()
                caller = callerActivity
                authListener = object : AuthStateListener {
                    override fun onAuthStateChanged(p0: FirebaseAuth) {
                        if (firebaseAuth.currentUser == null)
                            signIn()
                        Toast.makeText(callerActivity.baseContext, "Welcome back!", Toast.LENGTH_LONG).show()
                    }
                }
            }
            deals = arrayListOf()
            databaseReference = firebaseDatabase.reference.child(ref)
        }

        fun attachListener() {
            firebaseAuth.addAuthStateListener(authListener)
        }

        fun detachListener() {
            firebaseAuth.removeAuthStateListener(authListener)
        }

        private fun signIn() {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build())

            caller.startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN)
        }
    }
}