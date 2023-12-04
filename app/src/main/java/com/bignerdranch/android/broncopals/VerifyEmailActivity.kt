package com.bignerdranch.android.broncopals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bignerdranch.android.broncopals.databinding.ActivityVerifyEmailBinding
import com.google.firebase.auth.FirebaseAuth

class VerifyEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyEmailBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifyEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // verification button implemented, if verified, moves to ProfileCreationActivity
        binding.Verified.setOnClickListener {
            val user = firebaseAuth.currentUser
            if (user != null && user.isEmailVerified) {
                Toast.makeText(this, "Email has been verified! Let's continue.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ProfileCreationActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please verify email before trying to join the fun!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}