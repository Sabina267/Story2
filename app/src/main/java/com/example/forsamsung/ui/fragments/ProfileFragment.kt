package com.example.forsamsung.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forsamsung.R
import com.example.forsamsung.databinding.FragmentProfileBinding
import com.example.forsamsung.models.User
import com.example.forsamsung.utilits.UID
import com.example.forsamsung.utilits.USER
import com.example.forsamsung.utilits.userAdapter
import com.example.forsamsung.utilits.zapisiAdapter

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class ProfileFragment: Fragment(){
    private lateinit var binding: FragmentProfileBinding
    private lateinit var zapisiRecyclerView: RecyclerView
    private lateinit var zapisiArrayList: ArrayList<User>
    private lateinit var dbref: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        with(binding){
            userName.text = USER.username
            userStatus.text=USER.status
            userBio.text = USER.bio
            settings.setOnClickListener(){
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.profileFragment, SettingsFragment())
                transaction?.disallowAddToBackStack()
                transaction?.commit()
            }
            zapisiRecyclerView=zapisiList
            zapisiRecyclerView.layoutManager = LinearLayoutManager(activity)
            zapisiRecyclerView.setHasFixedSize(true)

            zapisiArrayList= arrayListOf<User>()
            getZapisiData()
        }
        return binding.root
    }

    private fun getZapisiData() {
        dbref = FirebaseDatabase.getInstance().getReference("zapisi").child(UID)
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
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}