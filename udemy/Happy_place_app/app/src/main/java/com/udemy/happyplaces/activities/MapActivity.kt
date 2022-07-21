package com.udemy.happyplaces.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.udemy.happyplaces.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity() {
    private var binding: ActivityMapBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}