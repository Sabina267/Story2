package com.example.forsamsung.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forsamsung.R
import com.example.forsamsung.activities.Home
import com.example.forsamsung.databinding.RandomFragmentBinding
import com.example.forsamsung.models.User
import com.example.forsamsung.utilits.*
import com.google.firebase.database.*

class RandomFragment(userId:String) :Fragment() {
    private lateinit var dbref: DatabaseReference
    private lateinit var binding: RandomFragmentBinding
    private lateinit var zapisiRecyclerView: RecyclerView
    private lateinit var zapisiArrayList: ArrayList<User>
    val userId: String
    init{
        this.userId = userId
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RandomFragmentBinding.inflate(inflater, container, false)
        with(binding){
            val view = requireActivity().findViewById<View>(com.example.forsamsung.R.id.bottomBar)
            view.isVisible = false
            REF_DATABASE_ROOT.child(NODE_USERS).child(userId).get().addOnSuccessListener {
                val userMap = it.value as? Map<*, *>
                val name = userMap?.get("username") as? String
                val status = userMap?.get("status") as? String
                val bio = userMap?.get("bio") as? String
                anotherUserName.text = name
                anotherUserStatus.text = status
                anotherUserBio.text = bio

                zapisiRecyclerView=anotherUserZapisiList
                zapisiRecyclerView.layoutManager = LinearLayoutManager(activity)
                zapisiRecyclerView.setHasFixedSize(true)

                zapisiArrayList= arrayListOf<User>()
                getZapisiData()
            }
            anotherBack.setOnClickListener() {
                replaceActivity(Home())
            }
        }
        return binding.root
    }

    private fun getZapisiData() {
        dbref = FirebaseDatabase.getInstance().getReference("zapisi").child(userId)
        dbref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnaphot in snapshot.children){
                        val user = userSnaphot.getValue(User::class.java)
                        zapisiArrayList.add(user!!)
                    }
                    zapisiRecyclerView.adapter = zapisiAdapter(zapisiArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}