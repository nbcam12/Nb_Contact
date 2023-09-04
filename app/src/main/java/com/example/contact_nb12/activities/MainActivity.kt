package com.example.contact_nb12.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.contact_nb12.R
import com.example.contact_nb12.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}