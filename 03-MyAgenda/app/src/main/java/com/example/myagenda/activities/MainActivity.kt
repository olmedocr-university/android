package com.example.myagenda.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myagenda.R
import com.example.myagenda.fragments.ItemFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = ItemFragment()
        transaction.add(R.id.activity_main_container, fragment)
        transaction.commit()
    }
}