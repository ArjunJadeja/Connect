package com.arjun.connect.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arjun.connect.R
import com.arjun.connect.data.User
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class SearchUsersAdapter(
    options: FirestoreRecyclerOptions<User>,
    private val listener: IAddUserAdapter
) :
    FirestoreRecyclerAdapter<User, SearchUsersAdapter.PostViewHolder>(options) {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.profileImageView)
        val name: TextView = itemView.findViewById(R.id.nameTextView)
        val username: TextView = itemView.findViewById(R.id.usernameTextView)
        val addUser: ImageButton = itemView.findViewById(R.id.addUserButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val viewHolder = PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.add_users_card, parent, false)
        )
        viewHolder.addUser.setOnClickListener {
            listener.onAddClicked(snapshots.getSnapshot(viewHolder.bindingAdapterPosition).id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: User) {
        holder.apply {
            Glide.with(itemView).load(model.profileImageUrl).into(holder.profileImage)
            name.text = model.name
            username.text = "@${model.username}"
        }
    }

}

interface IAddUserAdapter {
    fun onAddClicked(userId: String)
}
