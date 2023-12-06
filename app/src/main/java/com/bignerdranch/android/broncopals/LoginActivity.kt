package com.bignerdranch.android.broncopals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bignerdranch.android.broncopals.databinding.ActivityLoginBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.ChildEventListener
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


        binding.loginButton.setOnClickListener() {
            val username = binding.loginUsername.text.toString()
            val password = binding.loginPassword.text.toString()
            val currentUser = firebaseAuth.currentUser

//            databaseReference?.addValueEventListener(object: ValueEventListener) {
//                override fun onDataChange(snapshot: DataSnapshot) {
//
//                }
//            }

            if (username.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // checks for email verification using firebase authentication
                            if(currentUser!!.isEmailVerified) {
                                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, CreateProfileActivity::class.java)
//                                if (currentUser.hasProfile) {
//                                    val intent = Intent(this, CreateProfileActivity::class.java)
//                                }
//                                else {
//                                    val intent = Intent(this, MainActivity::class.java)
//                                }
                                startActivity(intent)
                                finish()
                            } else {
                                // if email still not verified, meaning fresh account, redirect to VerifyEmailActivity
                                Toast.makeText(this, "This email is not yet verified, please verify email!",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,LoginActivity::class.java))
                                finish()
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
}