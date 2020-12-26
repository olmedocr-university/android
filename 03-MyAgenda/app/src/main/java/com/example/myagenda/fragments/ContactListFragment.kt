package com.example.myagenda.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myagenda.R
import com.example.myagenda.activities.MainActivity
import com.example.myagenda.adapters.ContactsAdapter
import com.example.myagenda.database.ContactsDatabaseHandler
import com.example.myagenda.models.Contact
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_list_contact.*
import kotlinx.android.synthetic.main.fragment_list_contact.view.*
import java.io.*

private const val IMPORT_REQUEST_CODE = 1
private const val EXPORT_REQUEST_CODE = 2

class ContactListFragment : Fragment(), SearchView.OnQueryTextListener {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_contact, container, false)
        val dbHandler = ContactsDatabaseHandler(activity!!.applicationContext)

        with(view) {
            // Set the adapter
            recycler_contacts.layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            recycler_contacts.adapter = ContactsAdapter(dbHandler.getAllContacts(), activity as MainActivity)

            // Set the FAB
            fab_add.setOnClickListener {
                (activity as MainActivity).onAddButtonClicked()
            }

        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.menu_button_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
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
                if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE), EXPORT_REQUEST_CODE)
                    return
                } else {
                    Log.d("ContactListFragment", "exportContacts: permission already granted")
                }
            }
            val contacts = ContactsDatabaseHandler(context!!).getAllContacts()
            val json = Gson().toJson(contacts)
            writeOnSD(json)
            val snackbar = Snackbar.make(coordinator_layout_main, "Contacts correctly exported", Snackbar.LENGTH_LONG)
            snackbar.show()

        } catch (ex: Exception) {
            val snackbar = Snackbar.make(coordinator_layout_main, "Error while exporting contacts", Snackbar.LENGTH_LONG)
            snackbar.show()
            ex.printStackTrace()
        }

    }

    private fun importContacts() {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE), IMPORT_REQUEST_CODE)
                    return
                } else {
                    Log.d("ContactListFragment", "importContacts: permission already granted")
                }
            }
            val json = readFromSD()
            val dbHandler = ContactsDatabaseHandler(context!!)

            if (json == null) {
                val snackbar = Snackbar.make(coordinator_layout_main, "Error, the imported file is null", Snackbar.LENGTH_LONG)
                snackbar.show()
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
            val snackbar = Snackbar.make(coordinator_layout_main, "Contacts correctly imported", Snackbar.LENGTH_LONG)
            snackbar.show()

        } catch (ex: Exception) {
            val snackbar = Snackbar.make(coordinator_layout_main, "Error while importing contacts", Snackbar.LENGTH_LONG)
            snackbar.show()
            ex.printStackTrace()
        }
    }

    private fun writeOnSD(data: String) {
        if (isSdAvailable()) {
            try {
                val path = File(context!!.getExternalFilesDir(null), "contacts.cnt")
                val file = OutputStreamWriter(FileOutputStream(path))
                file.write(data)
                file.close()
            } catch (ex: Exception) {
                Log.e("ERROR", "Error al escribir fichero a tarjeta SD")
                val snackbar = Snackbar.make(coordinator_layout_main, "Error while writing to the SD", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        } else {
            Log.d("ERROR", "No ha sido posible crear archivos/carpetas")
            val snackbar = Snackbar.make(coordinator_layout_main, "Error, SD is not available", Snackbar.LENGTH_LONG)
            snackbar.show()

        }
    }

    private fun readFromSD(): String? {
        var data: String? = null
        if (isSdAvailable()) {
            try {
                val path = File(context!!.getExternalFilesDir(null), "contacts.cnt")
                val file = BufferedReader(InputStreamReader(FileInputStream(path)))
                data = file.readLine()
                file.close()
            } catch (ex: java.lang.Exception) {
                Log.e("ERROR", "Error al leer fichero desde tarjeta SD")
                val snackbar = Snackbar.make(coordinator_layout_main, "Error while reding from the SD", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        } else {
            Log.d("ERROR", "No ha sido posible crear archivos/carpetas");
            val snackbar = Snackbar.make(coordinator_layout_main, "Error, SD is not available", Snackbar.LENGTH_LONG)
            snackbar.show()
        }

        return data
    }

    private fun isSdAvailable(): Boolean {
        val storageState = Environment.getExternalStorageState()
        return storageState == Environment.MEDIA_MOUNTED
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            ContactListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}

interface OnAddButtonClickListener{
    fun onAddButtonClicked()
}