package com.example.forsamsung.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.forsamsung.activities.Home
import com.example.forsamsung.databinding.FragmentVhodBinding
import com.example.forsamsung.models.User
import com.example.forsamsung.utilits.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class VhodFragment: Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentVhodBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = Firebase.auth
        binding = FragmentVhodBinding.inflate(inflater, container, false)

        with(binding) {
            registracia.setOnClickListener() {
                replaceFragment(RegistrFragment())
                vhod.isVisible=false
            }

            vhod.setOnClickListener() {
                val email = emailVhod.text.toString()
                val password = passVhod.text.toString()
                if (check()){
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                        if (it.isSuccessful){
                            showToast("Ураа вы вошли в аккаунт")
                            USER.email=email
                            replaceActivity(Home())
                        }
                        else{
                            showToast("Неправильно введена почта или пароль((")
                        }
                    }
                }
            }

        }
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            replaceActivity(Home())
        }
    }

    private fun check(): Boolean {
        var proverka = 0
        with(binding) {
            val email = emailVhod.text.toString()

            if (FieldEmpty(emailVhod) or FieldEmpty(passVhod))
                proverka=1

            if (emailVhod.text.toString() != "" && !Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches()
            ) {
                emailVhod.error = "Проверь правильность почты!"
                proverka = 1
            }
            if (passVhod.text.toString() != "" && passVhod.length() < 7) {
                passVhod.error = "Пароль < 7 символов"
                proverka = 1
            }
            return proverka == 0
        }
    }

}


