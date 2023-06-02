package com.example.forsamsung.ui.fragments.oneChat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.forsamsung.R
import com.example.forsamsung.utilits.UID
import com.example.telegram.models.CommonModel


class OneNewChatAdapter : RecyclerView.Adapter<OneNewChatAdapter.SingleChatHolder>() {

    private var mListMessagesCache = emptyList<CommonModel>()

    class SingleChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val zapr: LinearLayout = view.findViewById(R.id.zaprosToUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_uved, parent, false)
        return SingleChatHolder(view)
    }
    override fun getItemCount(): Int = mListMessagesCache.size
    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        if (mListMessagesCache[position].from == UID) {
            holder.zapr.visibility=View.GONE
        } else {
            holder.zapr.visibility=View.VISIBLE
        }
    }
    fun setList(list: List<CommonModel>) {
        mListMessagesCache = list
    }
}
