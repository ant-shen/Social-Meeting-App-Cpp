package com.bignerdranch.android.broncopals;

public class UserAdapter {
    private val userList: List<UserProfile>) : RecyclerView.Adapter<UserViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            // Inflate your item layout here (e.g., user_item_layout.xml)
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item_layout, parent, false)
            return UserViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            val user = userList[position]
            holder.bind(user)
        }

        override fun getItemCount(): Int {
            return userList.size
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: UserProfile) {
            // itemView.findViewById<TextView>(R.id.usernameTextView).text = user.username
        }
    }
}
