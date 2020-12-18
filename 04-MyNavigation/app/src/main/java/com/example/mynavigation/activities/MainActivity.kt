package com.example.mynavigation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.mynavigation.R
import com.example.mynavigation.adapters.OnItemClickListener
import com.example.mynavigation.fragments.ContactDetailFragment
import com.example.mynavigation.fragments.ContactListFragment
import com.example.mynavigation.fragments.OnAddButtonClickListener
import com.example.mynavigation.models.Contact
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnItemClickListener, OnAddButtonClickListener {

    // TODO: handle orientation change in detail fragment
    // TODO: no snackbars shown
    // TODO: unable to import nor export
    // TODO: put two fragments side by side on portrait in list fragment
    // TODO: call action not working
    // TODO: add modify or delete an appointment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        goToList()

        setSupportActionBar(toolbar as Toolbar)
    }

    override fun onItemClicked(contact: Contact) {
        goToDetail(contact)
    }

    override fun onAddButtonClicked() {
        goToDetail(null)
    }

    fun goToList() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.title = "My Agenda"

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = ContactListFragment.newInstance(1)
        transaction.replace(R.id.activity_main_container, fragment)
        transaction.commit()
    }

    fun goToDetail(contact: Contact?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (contact != null) {
            supportActionBar?.title = contact.name
        } else {
            supportActionBar?.title = "New Contact"
        }

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = ContactDetailFragment.newInstance(contact)
        transaction.replace(R.id.activity_main_container, fragment)
        transaction.commit()
    }



}
