package com.bignerdranch.android.broncopals

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.bignerdranch.android.broncopals.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class UserProfile : Fragment(R.layout.fragment_user_profile) {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseRef : DatabaseReference
    private lateinit var storageRef : StorageReference
    private lateinit var dialog : Dialog
    private lateinit var user: UserData
    private lateinit var uid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        databaseRef = FirebaseDatabase.getInstance().reference.child("users")
        if (uid.isNotEmpty()) {
            getUserData()
        }

        binding.editProfileButton.setOnClickListener() {
            startActivity(Intent(activity, CreateProfileActivity::class.java))
        }
    }

    private fun getUserData() {
        databaseRef.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                user = snapshot.getValue(UserData::class.java)!!
                binding.fullName.setText(user.firstName + " " + user.lastName)
                binding.major.setText(user.major)
                binding.age.setText(user.age)
                binding.gender.setText(user.gender)
                binding.hobby.setText(user.hobbies)
                binding.bio.setText(user.aboutMe)

                Picasso.get().load(user.imageUri).into(binding.userImage)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


}