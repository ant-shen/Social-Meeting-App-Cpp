package com.bignerdranch.android.broncopals

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.broncopals.databinding.ActivityCreateProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateProfileBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = FirebaseAuth.getInstance().currentUser
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        binding.createProfileButton.setOnClickListener {
            val major = binding.major.text.toString()
            val age: Int? = binding.age.text.toString().toInt()
            val hobbies = binding.hobby.text.toString()
            val aboutMe = binding.aboutMe.text.toString()

            currentUser?.uid?.let { uid ->
                saveUserProfileToRealtimeDatabase(uid, major, age, hobbies, aboutMe)
            }
        }
    }
    private fun saveUserProfileToRealtimeDatabase(
        uid: String,
        major: String,
        age: Int?,
        hobbies: String,
        aboutMe: String
    ) {
        val user = hashMapOf(
            "major" to major,
            "age" to age,
            "hobbies" to hobbies,
            "aboutMe" to aboutMe
        )

        databaseReference.child(uid)
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile Created!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error! Profile Creation Unsuccessful", Toast.LENGTH_SHORT).show()
                //if currentUser.UID!
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
    }
}