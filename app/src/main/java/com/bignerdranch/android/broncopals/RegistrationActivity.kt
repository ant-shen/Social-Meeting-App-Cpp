package com.bignerdranch.android.broncopals

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.broncopals.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private val domain = "@cpp.edu" // Hard-coded domain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.signUpButton.setOnClickListener {
            val enteredUsername = binding.registerUsername.text.toString()
            val registerPassword = binding.registerPassword.text.toString()

            if (isValidEmail(enteredUsername) && registerPassword.isNotEmpty()) {
                // Append the domain to the entered username
                val username = "$enteredUsername$domain"
                registerUser(username, registerPassword)
            } else {
                Toast.makeText(this@RegistrationActivity, "Invalid email or password!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirect.setOnClickListener {
            startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        // Add your email validation logic here
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun registerUser(username: String, password: String) {
        databaseReference.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        auth.createUserWithEmailAndPassword(username, password)
                            .addOnCompleteListener(this@RegistrationActivity) { task ->
                                if (task.isSuccessful) {
                                    // Registration successful
                                    val user = auth.currentUser
                                    val userId = user?.uid
                                    val userData = UserData(userId, username, password)

                                    // Use the UID as the key for the user in the database
                                    databaseReference.child(userId!!).setValue(userData)

                                    Toast.makeText(this@RegistrationActivity, "Sign Up Successful!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
                                    finish()
                                } else {
                                    // If registration fails, display a message to the user.
                                    Toast.makeText(this@RegistrationActivity, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this@RegistrationActivity, "User Already Exists!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@RegistrationActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
