package com.example.forsamsung.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.forsamsung.R
import com.example.forsamsung.databinding.SetupProfileBinding
import com.example.forsamsung.utilits.replaceFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

@Suppress("DEPRECATION")
class SetupProfile : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    lateinit var binding: SetupProfileBinding
    private var uri = Uri.EMPTY
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SetupProfileBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        database = Firebase.database
        storage = Firebase.storage

        with(binding) {
            image.setOnClickListener() {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent,0)
            }

            gotovoButton.setOnClickListener(){
                replaceFragment(VhodFragment())
            }
        }

        return binding.root
    }



}