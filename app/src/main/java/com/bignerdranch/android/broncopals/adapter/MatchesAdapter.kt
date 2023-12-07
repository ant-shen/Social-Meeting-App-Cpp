package com.bignerdranch.android.broncopals.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.broncopals.UserData
import com.bignerdranch.android.broncopals.databinding.UserItemLayoutBinding
import com.bumptech.glide.Glide

class MatchesAdapter(val context : Context, val list: ArrayList<UserData>): RecyclerView.Adapter<MatchesAdapter.MatchesViewHolder>(){
    inner class MatchesViewHolder(val binding: UserItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        return MatchesViewHolder(UserItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        Glide.with(context).load(list[position].imageUri).into(holder.binding.userImage)

        holder.binding.firstName.text = list[position].firstName
    }
}