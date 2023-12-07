package com.bignerdranch.android.broncopals.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.broncopals.UserData
import com.bignerdranch.android.broncopals.databinding.ItemUserLayoutBinding
import com.bumptech.glide.Glide

class ProfileAdapter(val context : Context, val list : ArrayList<UserData>) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>(){
    inner class ProfileViewHolder(val binding: ItemUserLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        return ProfileViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.binding.textView6.text = list[position].firstName
        holder.binding.textView5.text = list[position].lastName

        Glide.with(context).load(list[position].imageUri).into(holder.binding.userImage)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}