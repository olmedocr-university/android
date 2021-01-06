package com.example.myagenda.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.myagenda.R
import com.example.myagenda.activities.MainActivity
import com.example.myagenda.database.MyAgendaDatabaseHandler
import com.example.myagenda.models.Contact
import kotlinx.android.synthetic.main.fragment_contact_detail.*

private const val ARG_PARAM1 = "param1"
private const val CALL_REQUEST_CODE = 1

/**
 * A simple [Fragment] subclass.
 * Use the [ContactDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactDetailFragment : Fragment() {
    private var contact: Contact? = null
    private var contactId: Long? = null
    var isUpdating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contact = it.getParcelable(ARG_PARAM1)
        }

        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbHelper = MyAgendaDatabaseHandler(context!!)

        if (contact != null) {
            // Editing existing contact
            contactId = contact?.id

            text_input_layout_name.editText?.setText(contact?.name)
            text_input_layout_address.editText?.setText(contact?.address)
            text_input_layout_telephone.editText?.setText(contact?.phone.toString())
            text_input_layout_mobile.editText?.setText(contact?.mobile.toString())
            text_input_layout_email.editText?.setText(contact?.email)

            isUpdating = true
        } else if (savedInstanceState != null) {
            // Loading from an orientation change
            text_input_layout_name.editText?.setText(contact?.name)
            text_input_layout_address.editText?.setText(contact?.address)
            text_input_layout_telephone.editText?.setText(contact?.phone.toString())
            text_input_layout_mobile.editText?.setText(contact?.mobile.toString())
            text_input_layout_email.editText?.setText(contact?.email)

            isUpdating = savedInstanceState.getBoolean("isUpdating")
        }

        if (isUpdating) {
            button_delete.visibility = View.VISIBLE
        } else {
            button_delete.visibility = View.GONE
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

            (activity as MainActivity).goToList()
        }

        button_delete.setOnClickListener {
            dbHelper.deleteContact(contactId!!)

            (activity as MainActivity).goToList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        if (isUpdating) {
            inflater.inflate(R.menu.menu_detail, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> (activity as MainActivity).goToList()
            R.id.menu_button_call -> callContact()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CALL_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callContact()
            }
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

    private fun callContact() {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(
                            arrayOf(Manifest.permission.CALL_PHONE),
                            CALL_REQUEST_CODE)
                    return
                } else {
                    Log.d("ContactDetailFragment", "callContact: permission already granted")
                }
            }
            val newIntent = Intent(Intent.ACTION_CALL)
            newIntent.data = Uri.parse("tel:${text_input_layout_mobile.editText?.text.toString()}")
            activity?.startActivity(newIntent)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: Contact?) =
            ContactDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                }
            }
    }
}