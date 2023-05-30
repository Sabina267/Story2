package com.example.forsamsung.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.forsamsung.activities.WriteStoryForMyself
import com.example.forsamsung.databinding.FragmentPlayBinding
import com.example.forsamsung.databinding.WriteStoryForMyselfBinding
import com.example.forsamsung.utilits.replaceActivity
import com.example.forsamsung.utilits.replaceFragment

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class PlayFragment: Fragment(){
    private lateinit var auth: FirebaseAuth
    lateinit var binding:FragmentPlayBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        with(binding){
            play1.setOnClickListener(){
                replaceActivity(WriteStoryForMyself())
            }
            play2.setOnClickListener(){
                replaceFragment(UsersChatOneFragment())
            }
            play3.setOnClickListener(){
                replaceFragment(NewChatUsersFragment())
            }
        }

        return binding.root
    }
}