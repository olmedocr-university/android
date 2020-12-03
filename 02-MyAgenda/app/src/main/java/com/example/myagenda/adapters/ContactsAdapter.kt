package com.example.myagenda.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myagenda.R
import com.example.myagenda.models.Contact
import kotlinx.android.synthetic.main.item_contact.view.*

class ContactsAdapter(var data: ArrayList<Contact>, val itemClickListener: OnItemClickListener): RecyclerView.Adapter<ContactsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact = data[position]
        holder.bind(contact, itemClickListener)

    }

    override fun getItemCount(): Int = data.size

    fun updateData(data: ArrayList<Contact>) {
        this.data = data
        this.notifyDataSetChanged()
    }
}

class ContactsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.text_view_name
    private val mobile: TextView = itemView.text_view_mobile

    fun bind(contact: Contact, clickListener: OnItemClickListener){
        name.text = contact.name
        mobile.text = contact.mobile.toString()

        itemView.setOnClickListener{
            clickListener.onItemClicked(contact)
        }
    }
}

interface OnItemClickListener{
    fun onItemClicked(contact: Contact)
}