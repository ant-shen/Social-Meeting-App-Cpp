package com.bignerdranch.android.broncopals

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.broncopals.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        binding.loginButton.setOnClickListener {
            val username = binding.loginUsername.text.toString()
            val password = binding.loginPassword.text.toString()
            val currentUser = firebaseAuth.currentUser
            if (username.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // checks for email verification using firebase authentication
                            if (currentUser!!.isEmailVerified) {
                                // Check if the user has a profile in the Realtime Database
                                //checkUserProfile(currentUser.uid)
                                startActivity(Intent(this@LoginActivity, CreateProfileActivity::class.java))
                            } else {
                                // if email still not verified, meaning a fresh account, deny login entry and display toast
                                Toast.makeText(this, "This email is not yet verified, please verify email!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this@LoginActivity, "ALL FIELDS MUST BE FILLED OUT!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerRedirect.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
            finish()
        }
    }
    private fun checkUserProfile(uid: String) {
        databaseReference.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User has a profile, redirect to MainActivity
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    // User does not have a profile, redirect to CreateProfileActivity
                    startActivity(Intent(this@LoginActivity, CreateProfileActivity::class.java))
                }
                finish()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error if needed
            }
        })
    }
}