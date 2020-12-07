package com.example.myagenda.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myagenda.R
import com.example.myagenda.adapters.ContactsAdapter
import com.example.myagenda.adapters.OnItemClickListener
import com.example.myagenda.database.ContactsDatabaseHandler
import com.example.myagenda.models.Contact
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import java.io.*

private const val LAUNCH_NEW_CONTACT_ACTIVITY: Int = 1
private const val LAUNCH_EXISTING_CONTACT_ACTIVITY: Int = 2

private const val IMPORT_REQUEST_CODE = 1
private const val EXPORT_REQUEST_CODE = 2


class MainActivity : AppCompatActivity(), OnItemClickListener, SearchView.OnQueryTextListener {
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

        val searchItem = menu?.findItem(R.id.menu_button_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(this)

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == IMPORT_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                importContacts()
            }
        } else if (requestCode == EXPORT_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportContacts()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LAUNCH_NEW_CONTACT_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                val dbHandler = ContactsDatabaseHandler(this)
                (recycler_contacts.adapter as ContactsAdapter).updateData(dbHandler.getAllContacts())
            }
        } else if (requestCode == LAUNCH_EXISTING_CONTACT_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                val dbHandler = ContactsDatabaseHandler(this)
                (recycler_contacts.adapter as ContactsAdapter).updateData(dbHandler.getAllContacts())
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        (recycler_contacts.adapter as ContactsAdapter).filter(newText!!)
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    private fun exportContacts() {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE), EXPORT_REQUEST_CODE)
                    return
                }
            }
            val contacts = ContactsDatabaseHandler(this).getAllContacts()
            val json = Gson().toJson(contacts)
            writeOnSD(json)

        } catch (ex: Exception) {
            // TODO: put sncakbar throwing error
            ex.printStackTrace()
        }

    }

    private fun importContacts() {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE), EXPORT_REQUEST_CODE)
                    return
                }
            }
            val json = readFromSD()
            val dbHandler = ContactsDatabaseHandler(this)

            if (json == null) {
                // TODO: put sncakbar throwing error
            } else {
                val itemType = object : TypeToken<ArrayList<Contact>>() {}.type
                val contacts = Gson().fromJson<ArrayList<Contact>>(json, itemType)

                dbHandler.dropAllContacts()
                for (contact in contacts) {
                    dbHandler.addContact(contact)
                }
            }
            val adapter = recycler_contacts.adapter as ContactsAdapter
            adapter.updateData(dbHandler.getAllContacts())
            // TODO: put sncakbar showing complete

        } catch (ex: Exception) {
            // TODO: put sncakbar throwing error
            ex.printStackTrace()
        }
    }

    private fun writeOnSD(data: String) {
        if (isSdAvailable()) {
            try {
                val path = File(this.getExternalFilesDir(null), "contacts.cnt")
                val file = OutputStreamWriter(FileOutputStream(path))
                file.write(data)
                file.close()
            } catch (ex: Exception) {
                // TODO: put snackbar aqui deerror
                Log.e("ERROR", "Error al escribir fichero a tarjeta SD")
            }
        } else {
            // TODO: put snackbar aqui de error
            Log.d("ERROR", "No ha sido posible crear archivos/carpetas")
        }
    }

    private fun readFromSD(): String? {
        var data: String? = null
        if (isSdAvailable()) {
            try {
                val path = File(this.getExternalFilesDir(null), "contacts.cnt")
                val file = BufferedReader(InputStreamReader(FileInputStream(path)))
                data = file.readLine()
                file.close()
            } catch (ex: java.lang.Exception) {
                // TODO: put snackbar aqui deerror
                Log.e("ERROR", "Error al leer fichero desde tarjeta SD")
            }
        } else {
            // TODO: put snackbar aqui deerror
            Log.d("ERROR", "No ha sido posible crear archivos/carpetas");
        }

        return data
    }

    private fun isSdAvailable(): Boolean {
        val storageState = Environment.getExternalStorageState()
        return storageState == Environment.MEDIA_MOUNTED
    }


}