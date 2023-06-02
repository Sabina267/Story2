package com.example.forsamsung.ui.fragments.oneChat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.forsamsung.databinding.FragmentOneChatBinding
import com.example.forsamsung.models.User
import com.example.forsamsung.ui.fragments.UsersChatOneFragment
import com.example.forsamsung.utilits.*
import com.example.telegram.models.CommonModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OneChatFragment(userId:String): Fragment() {
    private lateinit var binding: FragmentOneChatBinding
    private lateinit var mRefMessages:DatabaseReference
    private lateinit var mAdapter:OneChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListener:AppValueEventListener
    private var mListMessages = emptyList<CommonModel>()
    val userId: String
    init{
        this.userId = userId
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOneChatBinding.inflate(inflater, container, false)
        val view = requireActivity().findViewById<View>(com.example.forsamsung.R.id.bottomBar)
        view.isVisible = false
        with(binding){
            REF_DATABASE_ROOT.child(NODE_USERS).child(userId).get().addOnSuccessListener {
                val userMap = it.value as? Map<*, *>
                val name = userMap?.get("username") as? String
                companionName.text = name
            }

            back.setOnClickListener(){
                replaceFragment(UsersChatOneFragment())
            }

            sendMessage.setOnClickListener(){
                val message = chatInputMessage.text.toString()
                if (message.isEmpty()){
                    showToast("Введите сообщение")
                }
                else{
                    send(message, userId, TYPE_TEXT){
                        chatInputMessage.setText("")
                    }
                }
            }
            initRecyclerView()

        }
        return binding.root
    }

    private fun initRecyclerView() {
        mRecyclerView=binding.chatRecycleView
        mAdapter= OneChatAdapter()
        mRefMessages= REF_DATABASE_ROOT
            .child(NODE_MESSAGES)
            .child(UID)
            .child(userId)
        mRecyclerView.adapter=mAdapter
        mMessagesListener = AppValueEventListener { dataSnapshot->
            mListMessages=dataSnapshot.children.map {
                it.getCommonModel()
            }
            mAdapter.setList(mListMessages)
        }
        mRefMessages.addValueEventListener(mMessagesListener)
    }

    override fun onPause() {
        super.onPause()
        mRefMessages.removeEventListener(mMessagesListener)
    }

}