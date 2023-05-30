package com.example.forsamsung

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.forsamsung.databinding.ActivityMainBinding
import com.example.forsamsung.utilits.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    public override fun onStart() {
        super.onStart()
        initFirebase()
    }

    public override fun onStop() {
        super.onStop()
    }


}