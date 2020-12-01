package com.example.myagenda.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.myagenda.database.ContactsDatabaseHandler
import com.example.myagenda.R
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "New"

        val dbHelper = ContactsDatabaseHandler(this)
        val db = dbHelper.writableDatabase

        (toolbar as Toolbar).setNavigationOnClickListener {
            onBackPressed()
        }

        button_save.setOnClickListener {
            // Check fields
            // Store data in DB
            // TODO: check if it is an update or a create

        }

        button_delete.setOnClickListener {
            // Delete the current element in DB IF it exists, if not, just clear the text and go back
        }
    }
}