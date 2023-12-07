package com.bignerdranch.android.broncopals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bignerdranch.android.broncopals.ProfileFragment.Companion.list
import com.bignerdranch.android.broncopals.adapter.MatchesAdapter
import com.bignerdranch.android.broncopals.databinding.FragmentMatchesBinding

class MatchesFragment : Fragment(R.layout.fragment_matches){

    private lateinit var binding : FragmentMatchesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMatchesBinding.inflate(inflater, container, false)
        list = ArrayList()
        binding.recyclerView.adapter = MatchesAdapter(requireContext(), list!!)

        return binding.root
    }
}