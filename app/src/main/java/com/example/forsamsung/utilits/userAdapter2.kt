package com.example.forsamsung.utilits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.forsamsung.R
import com.example.forsamsung.models.User
import com.example.forsamsung.ui.fragments.UsersChatOneFragment


class userAdapter2(private val userList:ArrayList<User>, val listener: UsersChatOneFragment): RecyclerView.Adapter<userAdapter2.viewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent,false)
        return viewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = userList[position]
        holder.name.text = currentItem.username
        holder.status.text = currentItem.status
        holder.itemView.setOnClickListener{
            listener.onClick(currentItem.id)
        }
        //holder.itemView.setOnClickListener{replaceFragment(RandomFragment(currentItem.id))}


    }


    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.userNameItem)
        val status: TextView = itemView.findViewById(R.id.userStatusItem)

    }

    interface Listener{
        fun onClick(idUser:String)
    }
}