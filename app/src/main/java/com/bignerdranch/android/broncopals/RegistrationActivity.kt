package com.bignerdranch.android.broncopals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bignerdranch.android.broncopals.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.signUpButton.setOnClickListener {
            val registerUsername = binding.registerUsername.text.toString()
            val registerPassword = binding.registerPassword.text.toString()
            val registerConfirmPassword = binding.confirmPassword.text.toString()

            if (registerUsername.isNotEmpty() && registerPassword.isNotEmpty()){
                registerUser(registerUsername, registerPassword, registerConfirmPassword)
            } else {
                Toast.makeText(this@RegistrationActivity, "ALL FIELDS MUST BE FILLED OUT!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirect.setOnClickListener {
            startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser(username: String, password: String, confirmPassword: String) {
        if (confirmPassword == password ) {
            auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registration successful
                        val user = auth.currentUser

                        // Send email verification
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { emailVerificationTask ->
                                if (emailVerificationTask.isSuccessful) {
                                    Toast.makeText(
                                        this@RegistrationActivity,
                                        "Verification email sent.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@RegistrationActivity,
                                        "Failed to send verification email. Please use your school email address.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                        val userId = user?.uid
                        val userData = UserData(userId, username, password, false)

                        // Use the UID as the key for the user in the database
                        databaseReference.child(userId!!).setValue(userData)

                        Toast.makeText(
                            this@RegistrationActivity,
                            "Sign Up Successful! Check your email for verification.",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        // If registration fails, display a message to the user.
                        Toast.makeText(
                            this@RegistrationActivity,
                            "Registration failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(this@RegistrationActivity, "The passwords do not match! Try again.", Toast.LENGTH_SHORT).show()
        }
    }


}