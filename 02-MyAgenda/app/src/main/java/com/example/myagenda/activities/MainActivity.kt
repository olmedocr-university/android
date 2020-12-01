package com.example.myagenda.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.myagenda.R
import com.example.myagenda.database.ContactsDatabaseHandler
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar as Toolbar)

        fab_add.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }

        val dbHandler = ContactsDatabaseHandler(this)
        val cursor = dbHandler.getAllContacts()
        cursor!!.moveToFirst()

        print(cursor)

        cursor.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_button_export -> exportContacts()
            R.id.menu_button_import -> importContacts()
            else -> { // Note the block
                print("Search not implemented")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun exportContacts() {
        // TODO:
    }

    private fun importContacts() {
        // TODO:
    }
}