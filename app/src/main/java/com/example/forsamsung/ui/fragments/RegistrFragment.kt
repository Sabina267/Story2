package com.example.forsamsung.ui.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.forsamsung.databinding.FragmentRegisterBinding
import com.example.forsamsung.utilits.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrFragment: Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = Firebase.auth
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        with(binding) {
            voidite.setOnClickListener() {
                replaceFragment(VhodFragment())
            }
            zareg.setOnClickListener() {
                val email = emailReg.text.toString()
                val password = passwordReg.text.toString()
                val name = name.text.toString()
                if (check()){
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                        if (it.isSuccessful){


                            val uid = AUTH.currentUser?.uid.toString()
                            val dateMap = mutableMapOf<String, Any>()
                            dateMap[CHILD_ID] = uid
                            dateMap[CHILD_EMAIL] = email
                            dateMap[CHILD_USERNAME] = name
                            dateMap[CHILD_NAMEID]=uid
                            dateMap[CHILD_BIO]="У вас нет данных о себе"
                            dateMap[CHILD_STATUS]="Новичок"

                            REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap).addOnCompleteListener(){ task->
                                if (task.isSuccessful){
                                    auth.signOut()
                                    showToast("Вы зарегались")
                                    USER.username=name
                                    USER.id=uid
                                    replaceFragment(SetupProfile())
                                }
                                else{
                                    Toast.makeText(activity,"OSHIBKA", Toast.LENGTH_SHORT).show()
                                }
                            }

                        }
                        else{
                            Toast.makeText(activity,"Пользователь с такой почтой уже существует!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }
        return binding.root
    }

    private fun check(): Boolean {
        var proverka = 0
        with(binding) {
            val email = emailReg.text.toString()

            if (FieldEmpty(name) or FieldEmpty(emailReg) or FieldEmpty(passwordReg) or FieldEmpty(passwordRegPovt))
                proverka = 1

            if (emailReg.text.toString() != "" && !Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches()
            ) {
                emailReg.error = "Проверь правильность почты!"
                proverka = 1
            }
            if (passwordReg.text.toString() != "" && passwordReg.length() < 7) {
                passwordReg.error = "Пароль < 7 символов"
                proverka = 1
            }
            if (passwordReg.text.toString()!=passwordRegPovt.text.toString()){
                passwordRegPovt.error = "Пароль не совпадает с исходным"
                proverka=1
            }
            return proverka == 0
        }
    }
}


