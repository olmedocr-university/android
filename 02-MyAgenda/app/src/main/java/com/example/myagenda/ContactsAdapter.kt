package com.example.myagenda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myagenda.models.Contact
import kotlinx.android.synthetic.main.item_contact.view.*

class ContactsAdapter(private val data: Array<Contact>): RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.name.text = data[position].name
        holder.mobile.text = data[position].mobile.toString()
    }

    override fun getItemCount(): Int = data.size

    class ContactsViewHolder(itemsView: View): RecyclerView.ViewHolder(itemsView) {
        val name: TextView = itemsView.findViewById(R.id.text_view_name)
        val mobile: TextView = itemsView.findViewById(R.id.text_view_mobile)
    }
}