package com.example.forsamsung.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

import com.example.forsamsung.databinding.SettingsFragmentBinding
import com.example.forsamsung.utilits.*
import com.example.forsamsung.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*


class SettingsFragment: Fragment() {
    private lateinit var auth: FirebaseAuth
    lateinit var binding: SettingsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        with(binding) {
            val view = requireActivity().findViewById<View>(com.example.forsamsung.R.id.bottomBar)
            view.isVisible = false
            changeUserName.setText(USER.username)
            changeUserBio.setText(USER.bio)
            changeNameId.setText(USER.nameid)

            back.setOnClickListener() {
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                view.isVisible = true
                transaction?.replace(com.example.forsamsung.R.id.settingsFragment, ProfileFragment())
                transaction?.disallowAddToBackStack()
                transaction?.commit()
            }
            vihod.setOnClickListener(){
                auth.signOut()
                replaceActivity(MainActivity())
            }

            OK.setOnClickListener() {
                val name = changeUserName.text.toString()
                val bio = changeUserBio.text.toString()
                val nameid = changeNameId.text.toString().lowercase(Locale.getDefault())

                if (!FieldEmpty(changeUserName))
                    REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_USERNAME)
                        .setValue(name).addOnCompleteListener {
                            if (it.isSuccessful) {
                                USER.username = name
                            }

                        }
                if (!FieldEmpty(changeUserBio))
                    REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_BIO)
                        .setValue(bio).addOnCompleteListener {
                            if (it.isSuccessful) {
                                USER.bio=bio
                            }

                        }
                if (!FieldEmpty(changeNameId)) {
                    REF_DATABASE_ROOT.child(NODE_NAMEID)
                        .addListenerForSingleValueEvent(AppValueEventListener{
                            if (it.hasChild(nameid)){

                            }
                            else{
                                REF_DATABASE_ROOT.child(NODE_NAMEID).child(nameid)
                                    .setValue(UID)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_NAMEID)
                                                .setValue(nameid).addOnCompleteListener(){
                                                    if (it.isSuccessful){
                                                        REF_DATABASE_ROOT.child(NODE_NAMEID).child(USER.nameid).removeValue()
                                                            .addOnCompleteListener{
                                                                if (it.isSuccessful){
                                                                    showToast("Данные обновлены")
                                                                    USER.nameid=nameid
                                                                }
                                                            }
                                                    }
                                                }

                                        }

                                    }
                            }
                        })


                }
            }

        }
        return binding.root
    }



}
