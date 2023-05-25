package com.example.firefightersupportapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firefightersupportapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var biding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        biding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(biding.root)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.brigade1, FireBrigadeFragment())
        ft.replace(R.id.brigade2, FireBrigadeFragment())
        ft.replace(R.id.brigadeRic, FireBrigadeFragment())
        ft.commit()
    }
}