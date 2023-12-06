package com.bignerdranch.android.broncopals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")

        binding.createProfileButton.setOnClickListener {
            val userQuery = usersRef.orderByChild("username").equalTo(currentUser!!.email)

            val major = binding.major.text.toString()
            val age: Int? = binding.age.text.toString().toInt()
            val hobbies = binding.hobby.text.toString()
            val aboutMe = binding.aboutMe.text.toString()

            databaseReference = FirebaseDatabase.getInstance().getReference("users")
        }
    }
}