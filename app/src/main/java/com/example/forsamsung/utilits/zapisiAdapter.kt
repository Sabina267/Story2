package com.example.forsamsung.utilits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.forsamsung.R
import com.example.forsamsung.models.User
import java.sql.Types.NULL

class zapisiAdapter(private val zapisiList:ArrayList<User>): RecyclerView.Adapter<zapisiAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_zapisi, parent,false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: zapisiAdapter.viewHolder, position: Int) {
        val currentItem = zapisiList[position]
        holder.zagolovok.text = currentItem.zagolovok
        holder.zapis.text = currentItem.zapis
    }

    override fun getItemCount(): Int {
        return zapisiList.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val zagolovok : TextView = itemView.findViewById(R.id.zagolovokItem)
        val zapis: TextView = itemView.findViewById(R.id.zapisItem)
    }
}