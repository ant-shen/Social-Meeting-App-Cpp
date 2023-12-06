package com.bignerdranch.android.broncopals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bignerdranch.android.broncopals.databinding.ActivityCreateProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateProfileBinding
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
//        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")

        binding.createProfileButton.setOnClickListener {
            //val userQuery = usersRef.orderByChild("username").equalTo(currentUser!!.email)

            val major = binding.major.text.toString()
            val age = binding.age.text.toString()
            val hobbies = binding.hobby.text.toString()
            val aboutMe = binding.aboutMe.text.toString()

            updateProfile("true", major, age, hobbies, aboutMe)

            //databaseReference = FirebaseDatabase.getInstance().getReference("users")
        }
    }

    private fun updateProfile(hasProfile: String, major: String, age: String, hobbies: String, aboutMe: String) {
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
        val user = mapOf<String, String>(
            "hasProfile" to hasProfile,
            "major" to major,
            "age" to age,
            "hobbies" to hobbies,
            "aboutMe" to aboutMe
        )
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        usersRef.child(currentUser!!.uid).updateChildren(user).addOnSuccessListener {
            binding.major.text.clear()
            binding.age.text.clear()
            binding.hobby.text.clear()
            binding.aboutMe.text.clear()
            Toast.makeText(this, "Successfully Updated", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to Update", Toast.LENGTH_LONG).show()
        }
    }
}