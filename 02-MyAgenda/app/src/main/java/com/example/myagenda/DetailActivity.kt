package com.example.myagenda

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
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
            val values = ContentValues().apply {
                put(ContactsDbSchema.ContactsTable.Columns.KEY_CONTACT_NAME, text_input_layout_name.editText?.text.toString())
                put(ContactsDbSchema.ContactsTable.Columns.KEY_CONTACT_ADDRESS, text_input_layout_address.editText?.text.toString())
                put(ContactsDbSchema.ContactsTable.Columns.KEY_CONTACT_PHONE, text_input_layout_telephone.editText?.text.toString().toInt())
                put(ContactsDbSchema.ContactsTable.Columns.KEY_CONTACT_MOBILE, text_input_layout_mobile.editText?.text.toString().toInt())
                put(ContactsDbSchema.ContactsTable.Columns.KEY_CONTACT_EMAIL, text_input_layout_email.editText?.text.toString())
            }

            val newRowId = db?.insert(ContactsDbSchema.ContactsTable.TABLE_NAME, null, values)
            print("Inserted new contact with id $newRowId")
        }

        button_delete.setOnClickListener {
            // Delete the current element in DB IF it exists, if not, just clear the text and go back
        }
    }
}