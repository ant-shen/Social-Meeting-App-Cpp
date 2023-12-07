package com.bignerdranch.android.broncopals

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.broncopals.databinding.ActivityCreateProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CreateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateProfileBinding
    private lateinit var storageReference: StorageReference

    private var uri: Uri? = null
    private val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storageReference = FirebaseStorage.getInstance().reference
        var imageUri = ""

        // goes into gallery to pick an image to upload
        val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding.images.setImageURI(it)
            if (it != null) {
                // stores the image into variable uri
                uri = it
            } else {
                Toast.makeText(this, "The images was not selected or there was an error!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.uploadImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")

        }

        binding.createProfileButton.setOnClickListener {
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val major = binding.major.text.toString()
            val gender = binding.major.text.toString()
            val age = binding.age.text.toString()
            val hobbies = binding.hobby.text.toString()
            val aboutMe = binding.aboutMe.text.toString()
            //val userQuery = usersRef.orderByChild("username").equalTo(currentUser!!.email)
            uri?.let {
                storageReference.child(currentUser!!.uid).putFile(it)
                    .addOnSuccessListener { task ->
                        task.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener { url ->
                                Toast.makeText(this, "Image successfully stored!", Toast.LENGTH_SHORT).show()
                                imageUri = url.toString()
                                //updatePicture(imageUri)
                                //updateProfile(firstName, lastName, major, gender, age, hobbies, aboutMe)
                            }
                    } .addOnFailureListener {
                        Toast.makeText(this, "Image storage has failed!", Toast.LENGTH_SHORT).show()
                    }
                updateProfile(imageUri, firstName, lastName, major, gender, age, hobbies, aboutMe)
            }

            if (imageUri == "") {
                Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show()
            }
            //databaseReference = FirebaseDatabase.getInstance().getReference("users")
        }
    }

    private fun updateProfile(imageUri:String, firstName: String, lastName: String, major: String,gender: String, age: String, hobbies: String, aboutMe: String) {
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
        val user = mapOf(
            "imageUri" to imageUri,
            "firstName" to firstName,
            "lastName" to lastName,
            "major" to major,
            "gender" to gender,
            "age" to age,
            "hobbies" to hobbies,
            "aboutMe" to aboutMe,
            "hasProfile" to true
        )

        usersRef.child(currentUser!!.uid).updateChildren(user).addOnSuccessListener {
            binding.firstName.text.clear()
            binding.lastName.text.clear()
            binding.major.text.clear()
            binding.gender.text.clear()
            binding.age.text.clear()
            binding.hobby.text.clear()
            binding.aboutMe.text.clear()

            Toast.makeText(this, "Successfully Updated", Toast.LENGTH_LONG).show()

            val intent = Intent(this@CreateProfileActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to Update", Toast.LENGTH_LONG).show()
        }
    }
}