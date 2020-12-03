package com.example.myagenda.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.myagenda.R
import com.example.myagenda.database.ContactsDatabaseHandler
import com.example.myagenda.models.Contact
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO: add scroll to avoid having to close the keyboard (and useful for landscape)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "New contact"

        if (savedInstanceState != null) {
            // Loading from an orientation change
            val contact = savedInstanceState.getParcelable<Contact>("Contact")

            text_input_layout_name.editText?.setText(contact?.name)
            text_input_layout_address.editText?.setText(contact?.address)
            text_input_layout_telephone.editText?.setText(contact?.phone.toString())
            text_input_layout_mobile.editText?.setText(contact?.mobile.toString())
            text_input_layout_email.editText?.setText(contact?.email)

            supportActionBar?.title = contact?.name

        } else if (intent.extras != null) {
            // Editing existing contact
            val contact = intent.extras?.getParcelable<Contact>("Contact")

            text_input_layout_name.editText?.setText(contact?.name)
            text_input_layout_address.editText?.setText(contact?.address)
            text_input_layout_telephone.editText?.setText(contact?.phone.toString())
            text_input_layout_mobile.editText?.setText(contact?.mobile.toString())
            text_input_layout_email.editText?.setText(contact?.email)

            supportActionBar?.title = contact?.name
        }


        val dbHelper = ContactsDatabaseHandler(this)

        (toolbar as Toolbar).setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            onBackPressed()
        }

        button_save.setOnClickListener {
            // TODO: Check fields for errors
            // FIXME: no guarda cuando editas
            dbHelper.addContact(Contact(
                    text_input_layout_name.editText?.text.toString(),
                    text_input_layout_address.editText?.text.toString(),
                    text_input_layout_telephone.editText?.text.toString().toInt(),
                    text_input_layout_mobile.editText?.text.toString().toInt(),
                    text_input_layout_email.editText?.text.toString()))

            val returnIntent: Intent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        button_delete.setOnClickListener {
            // TODO: Delete the current element in DB IF it exists, if not, just go back
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val contact = Contact(
                text_input_layout_name.editText?.text.toString(),
                text_input_layout_address.editText?.text.toString(),
                text_input_layout_telephone.editText?.text.toString().toInt(),
                text_input_layout_mobile.editText?.text.toString().toInt(),
                text_input_layout_email.editText?.text.toString())

        outState.putParcelable("Contact", contact)

        super.onSaveInstanceState(outState)
    }
}