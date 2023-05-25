package com.example.firefightersupportapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firefightersupportapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var biding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        biding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(biding.root)
    }
}