package com.example.forsamsung.ui.fragments.oneChat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.forsamsung.R
import com.example.forsamsung.utilits.UID
import com.example.telegram.models.CommonModel


class OneChatAdapter : RecyclerView.Adapter<OneChatAdapter.SingleChatHolder>() {

    private var mListMessagesCache = emptyList<CommonModel>()

    class SingleChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val blocUserMessage: ConstraintLayout = view.findViewById(R.id.bloc_user_message)
        val chatUserMessage: TextView = view.findViewById(R.id.chat_user_message)
        val blocReceivedMessage: ConstraintLayout = view.findViewById(R.id.bloc_received_message)
        val chatReceivedMessage: TextView = view.findViewById(R.id.chat_received_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return SingleChatHolder(view)
    }
    override fun getItemCount(): Int = mListMessagesCache.size
    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        if (mListMessagesCache[position].from == UID) {
            holder.blocUserMessage.visibility = View.VISIBLE
            holder.blocReceivedMessage.visibility = View.GONE
            holder.chatUserMessage.text = mListMessagesCache[position].text
        } else {
            holder.blocUserMessage.visibility = View.GONE
            holder.blocReceivedMessage.visibility = View.VISIBLE
            holder.chatReceivedMessage.text = mListMessagesCache[position].text
        }
    }
    fun setList(list: List<CommonModel>) {
        mListMessagesCache = list
        notifyDataSetChanged()
    }
}
