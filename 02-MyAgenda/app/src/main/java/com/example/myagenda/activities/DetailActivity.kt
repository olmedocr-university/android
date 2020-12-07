package com.example.myagenda.activities

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.example.myagenda.R
import com.example.myagenda.database.ContactsDatabaseHandler
import com.example.myagenda.models.Contact
import kotlinx.android.synthetic.main.activity_detail.*

private const val CALL_REQUEST_CODE = 1

class DetailActivity : AppCompatActivity() {
    private var contactId: Long? = null
    var isUpdating = false

    override fun onCreate(savedInstanceState: Bundle?) {
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

            isUpdating = savedInstanceState.getBoolean("isUpdating")

            supportActionBar?.title = contact?.name

        } else if (intent.extras != null) {
            // Editing existing contact
            val contact = intent.extras?.getParcelable<Contact>("Contact")

            contactId = contact?.id

            text_input_layout_name.editText?.setText(contact?.name)
            text_input_layout_address.editText?.setText(contact?.address)
            text_input_layout_telephone.editText?.setText(contact?.phone.toString())
            text_input_layout_mobile.editText?.setText(contact?.mobile.toString())
            text_input_layout_email.editText?.setText(contact?.email)

            isUpdating = true

            supportActionBar?.title = contact?.name
        }

        if (isUpdating) {
            button_delete.visibility = VISIBLE
        } else {
            button_delete.visibility = GONE
        }

        val dbHelper = ContactsDatabaseHandler(this)

        (toolbar as Toolbar).setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            onBackPressed()
        }

        button_save.setOnClickListener {
            // TODO: Check fields for errors
            if (isUpdating) {
                val updatedContact = Contact(
                        contactId,
                        text_input_layout_name.editText?.text.toString(),
                        text_input_layout_address.editText?.text.toString(),
                        text_input_layout_telephone.editText?.text.toString(),
                        text_input_layout_mobile.editText?.text.toString(),
                        text_input_layout_email.editText?.text.toString())

                dbHelper.updateContact(updatedContact)

            } else {
                val newContact = Contact(
                        null,
                        text_input_layout_name.editText?.text.toString(),
                        text_input_layout_address.editText?.text.toString(),
                        text_input_layout_telephone.editText?.text.toString(),
                        text_input_layout_mobile.editText?.text.toString(),
                        text_input_layout_email.editText?.text.toString())

                val newContactId = dbHelper.addContact(newContact)
                newContact.id = newContactId
            }

            val returnIntent: Intent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        button_delete.setOnClickListener {
            dbHelper.deleteContact(contactId!!)

            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isUpdating) {
            menuInflater.inflate(R.menu.menu_detail, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_button_call -> callContact()
            else -> {
                print("Something went wrong")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CALL_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callContact()
            }
        }
    }

    private fun callContact() {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(
                            Manifest.permission.CALL_PHONE), CALL_REQUEST_CODE)
                    return
                }
            }
            val newIntent = Intent(ACTION_CALL)
            newIntent.data = Uri.parse("tel:${text_input_layout_mobile.editText?.text.toString()}")
            startActivity(newIntent)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val contact = Contact(
                contactId,
                text_input_layout_name.editText?.text.toString(),
                text_input_layout_address.editText?.text.toString(),
                text_input_layout_telephone.editText?.text.toString(),
                text_input_layout_mobile.editText?.text.toString(),
                text_input_layout_email.editText?.text.toString())

        outState.putParcelable("Contact", contact)
        outState.putBoolean("isUpdating", isUpdating)

        super.onSaveInstanceState(outState)
    }
}