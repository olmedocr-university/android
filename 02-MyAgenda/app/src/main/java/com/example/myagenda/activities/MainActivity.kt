package com.example.myagenda.activities

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myagenda.ContactsAdapter
import com.example.myagenda.R
import com.example.myagenda.database.ContactsDatabaseHandler
import com.example.myagenda.database.ContactsDatabaseHandler.Companion.KEY_CONTACT_ADDRESS
import com.example.myagenda.database.ContactsDatabaseHandler.Companion.KEY_CONTACT_EMAIL
import com.example.myagenda.database.ContactsDatabaseHandler.Companion.KEY_CONTACT_ID
import com.example.myagenda.database.ContactsDatabaseHandler.Companion.KEY_CONTACT_MOBILE
import com.example.myagenda.database.ContactsDatabaseHandler.Companion.KEY_CONTACT_NAME
import com.example.myagenda.database.ContactsDatabaseHandler.Companion.KEY_CONTACT_PHONE
import com.example.myagenda.models.Contact
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar as Toolbar)

        fab_add.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivityForResult(intent, )
        }

        val dbHandler = ContactsDatabaseHandler(this)
        val cursor = dbHandler.getAllContacts()
        val data = getDataFromCursor(cursor!!)

        recycler_contacts.adapter = ContactsAdapter(data)
        recycler_contacts.layoutManager = LinearLayoutManager(this)
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

    private fun getDataFromCursor(cursor: Cursor): ArrayList<Contact>{
        var contacts: ArrayList<Contact> = ArrayList()

        if (!cursor.moveToFirst()) {
            return contacts
        }

        val idIndex = cursor.getColumnIndex(KEY_CONTACT_ID)
        val nameIndex = cursor.getColumnIndex(KEY_CONTACT_NAME)
        val addressIndex = cursor.getColumnIndex(KEY_CONTACT_ADDRESS)
        val phoneIndex = cursor.getColumnIndex(KEY_CONTACT_PHONE)
        val mobileIndex = cursor.getColumnIndex(KEY_CONTACT_MOBILE)
        val emailIndex = cursor.getColumnIndex(KEY_CONTACT_EMAIL)

        do {
            contacts.add(Contact(
                        cursor.getString(nameIndex),
                        cursor.getString(addressIndex),
                        cursor.getInt(phoneIndex),
                        cursor.getInt(mobileIndex),
                        cursor.getString(emailIndex)
                    ))
        } while (cursor.moveToNext())

        cursor.close()

        return contacts
    }
}