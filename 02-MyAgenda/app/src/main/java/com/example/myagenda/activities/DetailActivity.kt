package com.example.myagenda.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.myagenda.database.ContactsDatabaseHandler
import com.example.myagenda.R
import com.example.myagenda.models.Contact
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.item_contact.*


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "New"

        val dbHelper = ContactsDatabaseHandler(this)

        (toolbar as Toolbar).setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            onBackPressed()
        }

        button_save.setOnClickListener {
            // Check fields
            // TODO: check if it is an update or a create
            dbHelper.addContact(Contact(
                    text_input_layout_name.editText?.text.toString(),
                    text_input_layout_address.editText?.text.toString(),
                    text_input_layout_telephone.editText?.text.toString().toInt(),
                    text_input_layout_mobile.editText?.text.toString().toInt(),
                    text_input_layout_email.editText?.text.toString()))

            setResult(RESULT_OK)
            finish()

        }

        button_delete.setOnClickListener {
            // Delete the current element in DB IF it exists, if not, just clear the text and go back
        }
    }
}