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

class AddedUsersAdapter(
    options: FirestoreRecyclerOptions<User>,
    private val listener: IRemoveUserAdapter
) :
    FirestoreRecyclerAdapter<User, AddedUsersAdapter.PostViewHolder>(options) {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.userProfileImage)
        val name: TextView = itemView.findViewById(R.id.nameText)
        val username: TextView = itemView.findViewById(R.id.usernameText)
        val status: TextView = itemView.findViewById(R.id.statusText)
        val removeUserButton: ImageButton = itemView.findViewById(R.id.removeUserButton)
        val linkedInProfileButton: ImageButton = itemView.findViewById(R.id.linkedInProfileButton)
        val twitterProfileButton: ImageButton = itemView.findViewById(R.id.twitterProfileButton)
        val instagramProfileButton: ImageButton = itemView.findViewById(R.id.instagramProfileButton)
        val whatsappProfileButton: ImageButton = itemView.findViewById(R.id.whatsappProfileButton)
        val phoneProfileButton: ImageButton = itemView.findViewById(R.id.phoneProfileButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val viewHolder = PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_profile_card, parent, false)
        )
        viewHolder.removeUserButton.setOnClickListener {
            listener.onRemoveClicked(snapshots.getSnapshot(viewHolder.bindingAdapterPosition).id)
        }
        viewHolder.linkedInProfileButton.setOnClickListener {
            listener.linkedInProfile(
                snapshots.getSnapshot(viewHolder.bindingAdapterPosition)
                    .toObject(User::class.java)?.linkedInUserName.toString()
            )
        }
        viewHolder.twitterProfileButton.setOnClickListener {
            listener.twitterProfile(
                snapshots.getSnapshot(viewHolder.bindingAdapterPosition)
                    .toObject(User::class.java)?.twitterUserName.toString()
            )
        }
        viewHolder.instagramProfileButton.setOnClickListener {
            listener.instagramProfile(
                snapshots.getSnapshot(viewHolder.bindingAdapterPosition)
                    .toObject(User::class.java)?.instagramUserName.toString()
            )
        }
        viewHolder.whatsappProfileButton.setOnClickListener {
            listener.whatsAppProfile(
                snapshots.getSnapshot(viewHolder.bindingAdapterPosition)
                    .toObject(User::class.java)?.whatsAppNumber.toString()
            )
        }
        viewHolder.phoneProfileButton.setOnClickListener {
            listener.phoneProfile(
                snapshots.getSnapshot(viewHolder.bindingAdapterPosition)
                    .toObject(User::class.java)?.whatsAppNumber.toString()
            )
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: User) {
        holder.apply {
            Glide.with(itemView).load(model.profileImageUrl).into(holder.profileImage)
            name.text = model.name
            username.text = "@${model.username}"
            status.text = "Status: ${model.status}"
        }

        if (model.linkedInUserName.toString().isEmpty()) {
            holder.linkedInProfileButton.visibility = View.GONE
        }
        if (model.twitterUserName.toString().isEmpty()) {
            holder.twitterProfileButton.visibility = View.GONE
        }
        if (model.instagramUserName.toString().isEmpty()) {
            holder.instagramProfileButton.visibility = View.GONE
        }
        if (model.whatsAppNumber.toString().isEmpty()) {
            holder.whatsappProfileButton.visibility = View.GONE
            holder.phoneProfileButton.visibility = View.GONE
        }
    }
}

interface IRemoveUserAdapter {
    fun onRemoveClicked(userId: String)
    fun linkedInProfile(linkedInUserName: String)
    fun twitterProfile(twitterUserName: String)
    fun instagramProfile(instagramUserName: String)
    fun whatsAppProfile(whatsappNumber: String)
    fun phoneProfile(phoneNumber: String)
}