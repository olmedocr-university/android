package com.example.myagenda.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.myagenda.R
import com.example.myagenda.adapters.OnItemClickListener
import com.example.myagenda.fragments.AgendaFragment
import com.example.myagenda.fragments.ContactDetailFragment
import com.example.myagenda.fragments.ContactListFragment
import com.example.myagenda.fragments.OnAddButtonClickListener
import com.example.myagenda.models.Contact
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), OnItemClickListener, OnAddButtonClickListener {

    var isPortrait = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val orientation = this.resources.configuration.orientation
        isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT

        if (savedInstanceState != null) {
            // rotation change
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            val mainFragment = manager.findFragmentById(R.id.activity_main_container)
            val listFragment = manager.findFragmentById(R.id.fragment_list_container)
            val detailFragment = manager.findFragmentById(R.id.fragment_detail_container)

            if (mainFragment != null) {
                transaction.remove(mainFragment)
            }

            if (detailFragment != null) {
                transaction.remove(detailFragment)
            }

            if (listFragment != null) {
                transaction.remove(listFragment)
            }

            transaction.commit()
        }

        goToList()

        setSupportActionBar(toolbar as Toolbar)
    }

    override fun onItemClicked(contact: Contact) {
        goToDetail(contact)
    }

    override fun onAddButtonClicked() {
        goToDetail(null)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_button_agenda -> goToCalendar()
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    fun goToList() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.title = "My Agenda"

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = ContactListFragment.newInstance(1)

        if (isPortrait) {
            transaction.replace(R.id.activity_main_container, fragment)
        } else {
            transaction.replace(R.id.fragment_list_container, fragment)
            manager.findFragmentById(R.id.fragment_detail_container)?.let { transaction.remove(it) }
        }

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

        if (isPortrait) {
            transaction.replace(R.id.activity_main_container, fragment)
        } else {
            transaction.replace(R.id.fragment_detail_container, fragment)
        }

        transaction.commit()

    }

    fun goToCalendar() {
        val intent = Intent(this, CalendarActivity::class.java)
        startActivity(intent)
    }

}
