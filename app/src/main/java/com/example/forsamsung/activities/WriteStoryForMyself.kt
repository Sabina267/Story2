package com.example.forsamsung.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.forsamsung.databinding.WriteStoryForMyselfBinding
import com.example.forsamsung.models.User
import com.example.forsamsung.ui.fragments.SetupProfile
import com.example.forsamsung.utilits.*
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class WriteStoryForMyself : AppCompatActivity() {
    lateinit var binding: WriteStoryForMyselfBinding
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WriteStoryForMyselfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            addZapis.setOnClickListener(){
                if (!FieldEmpty(zapis) && !FieldEmpty(zagolovok)){
                    val textZapis = zapis.text.toString()
                    val textZagolovok = zagolovok.text.toString()
                     if (textZapis.isEmpty()){

                     }
                    else{
                        saveZapis(textZapis, textZagolovok){
                            zapis.setText("")
                            zagolovok.setText("")
                            replaceActivity(Home())
                        }
                     }
                }
            }
            back.setOnClickListener(){
                replaceActivity(Home())
            }

        }
    }


}