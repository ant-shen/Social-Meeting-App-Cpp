package com.bignerdranch.android.broncopals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bignerdranch.android.broncopals.adapter.MatchesAdapter
import com.bignerdranch.android.broncopals.databinding.FragmentMatchesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MatchesFragment : Fragment(R.layout.fragment_matches) {

    private lateinit var binding: FragmentMatchesBinding
    private lateinit var matchesAdapter: MatchesAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMatchesBinding.inflate(inflater, container, false)
        matchesAdapter = MatchesAdapter(requireContext(), ArrayList())
        binding.recyclerView.adapter = matchesAdapter

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        // Fetch user data and populate the list
        fetchUserData()

        return binding.root
    }

    private fun fetchUserData() {
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = ArrayList<UserData>()

                for (data in snapshot.children) {
                    val user = data.getValue(UserData::class.java)

                    // Exclude the current user from the list
                    if (user?.username != currentUserEmail) {
                        user?.let {
                            userList.add(it)
                        }
                    }
                }

                // Update the adapter with the new data
                matchesAdapter.updateData(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error if needed
            }
        })
    }
}
