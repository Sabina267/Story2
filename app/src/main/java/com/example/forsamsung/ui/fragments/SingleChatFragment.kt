package com.example.forsamsung.ui.fragments

import android.os.Build.ID
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.forsamsung.databinding.FragmentSingleChatBinding
import com.example.forsamsung.ui.fragments.oneChat.OneChatAdapter
import com.example.forsamsung.utilits.*
import com.example.telegram.models.CommonModel
import com.google.firebase.database.*

class SingleChatFragment(userIdTo:String, userIdFrom:String): Fragment() {
    private lateinit var binding: FragmentSingleChatBinding
    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListener: AppValueEventListener
    private var itog =""
    private var mListMessages = emptyList<CommonModel>()
    private lateinit var text:String
    val userTo: String
    val userFrom:String
    init{
        this.userTo = userIdTo
        this.userFrom = userIdFrom
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        val view = requireActivity().findViewById<View>(com.example.forsamsung.R.id.bottomBar)
        view.isVisible = false
        with(binding){
            if (userTo == USER.id){
                REF_DATABASE_ROOT.child(NODE_USERS).child(userFrom).get().addOnSuccessListener {
                    val userMap = it.value as? Map<*, *>
                    val name = userMap?.get("username") as? String
                    companionName1.text = name
                }
            }
            else{
                REF_DATABASE_ROOT.child(NODE_USERS).child(userTo).get().addOnSuccessListener {
                    val userMap = it.value as? Map<*, *>
                    val name = userMap?.get("username") as? String
                    companionName1.text = name
                }
            }


            back1.setOnClickListener(){
                replaceFragment(Notifications())
            }

            konec.setOnClickListener(){
                val database = FirebaseDatabase.getInstance()
                val messageOneRef = database.getReference("messageOne")
                val idUser1Ref = messageOneRef.child(userFrom).child(userTo)
                idUser1Ref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        itog=""
                        for (idUser2Snapshot in dataSnapshot.children) {
                            itog+=idUser2Snapshot.child("text").getValue(String::class.java)+". "
                        }

                        REF_DATABASE_ROOT.child("messageOne").child(userTo).child(userFrom).removeValue()
                        REF_DATABASE_ROOT.child("messageOne").child(userFrom).child(userTo).removeValue()
                        if (itog!=""){

                            REF_DATABASE_ROOT.child("notification").child(userTo).addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (childSnapshot in dataSnapshot.children) {

                                        if (childSnapshot.child("from").toString()==userFrom && childSnapshot.child("from").toString()==userTo ||
                                            childSnapshot.child("to").toString()==userFrom && childSnapshot.child("to").toString()==userTo){

                                        }
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                            var text = "Совместная работа"
                            saveZapisId(itog, text, userTo){}
                            saveZapisId(itog, text, userFrom){
                                replaceFragment(Notifications())
                            }
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }
            sendMessage1.setOnClickListener(){
                val message = chatInputMessage1.text.toString()
                if (message.isEmpty()){
                    showToast("Введите сообщение")
                }
                else{
                    val message = chatInputMessage1.text.toString()
                    if (message.isEmpty()){
                        showToast("Введите сообщение")
                    }
                    else{
                        if (userTo == USER.id){
                            sendOne(message, userFrom, TYPE_TEXT){
                                chatInputMessage1.setText("")
                            }
                        }
                        else{
                            sendOne(message, userTo, TYPE_TEXT){
                                chatInputMessage1.setText("")
                            }
                        }
                    }
                }

            }
            initRecyclerView()

        }
        return binding.root
    }

    private fun initRecyclerView() {
        mRecyclerView=binding.chatRecycleView1
        mAdapter= SingleChatAdapter()
        if (userTo == USER.id){
            mRefMessages= REF_DATABASE_ROOT
                .child(NODE_MESSAGES_ONE)
                .child(UID)
                .child(userFrom)
        }
        else{
            mRefMessages= REF_DATABASE_ROOT
                .child(NODE_MESSAGES_ONE)
                .child(UID)
                .child(userTo)
        }
        mRecyclerView.adapter=mAdapter
        mMessagesListener = AppValueEventListener { dataSnapshot->
            mListMessages=dataSnapshot.children.map {
                it.getCommonModel()
            }
            mAdapter.setList(mListMessages)
        }
        mRefMessages.addValueEventListener(mMessagesListener)
    }
}