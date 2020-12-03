package com.example.myagenda.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myagenda.R
import com.example.myagenda.adapters.ContactsAdapter
import com.example.myagenda.adapters.OnItemClickListener
import com.example.myagenda.database.ContactsDatabaseHandler
import com.example.myagenda.models.Contact
import kotlinx.android.synthetic.main.activity_main.*

private const val LAUNCH_NEW_CONTACT_ACTIVITY: Int = 1
private const val LAUNCH_EXISTING_CONTACT_ACTIVITY: Int = 2

class MainActivity : AppCompatActivity(), OnItemClickListener {
    // TODO: implement search

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar as Toolbar)

        fab_add.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivityForResult(intent, LAUNCH_NEW_CONTACT_ACTIVITY)
        }

        val dbHandler = ContactsDatabaseHandler(this)
        recycler_contacts.adapter = ContactsAdapter(dbHandler.getAllContacts(), this)
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
            else -> {
                print("Search not implemented")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClicked(contact: Contact) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("Contact", contact)

        startActivityForResult(intent, LAUNCH_EXISTING_CONTACT_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LAUNCH_NEW_CONTACT_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                val dbHandler = ContactsDatabaseHandler(this)
                (recycler_contacts.adapter as ContactsAdapter).updateData(dbHandler.getAllContacts())
            }
        } else if (requestCode == LAUNCH_EXISTING_CONTACT_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                // TODO: update ONLY the edited contact
                val dbHandler = ContactsDatabaseHandler(this)

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun exportContacts() {
        // TODO: implement function
    }

    private fun importContacts() {
        // TODO: implement function
    }


}