package com.example.mycontacts.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.mycontacts.R
import com.example.mycontacts.models.Contact
import kotlinx.android.synthetic.main.item_contact.view.*
import java.util.*
import kotlin.collections.ArrayList


class ContactsAdapter(
        var data: ArrayList<Contact>,
        val itemClickListener: OnItemClickListener
): RecyclerView.Adapter<ContactsViewHolder>() {
    var dataSnapshot: ArrayList<Contact> = ArrayList()

    init {
        this.dataSnapshot.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_contact,
            parent,
            false
        )
        return ContactsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact = data[position]
        holder.bind(contact, itemClickListener)
    }

    override fun getItemCount(): Int = data.size

    fun updateData(data: ArrayList<Contact>) {
        this.data = data
        this.dataSnapshot.addAll(data)
        this.notifyDataSetChanged()
    }

    fun filter(text: String) {
        data.clear()
        if (text.isEmpty()) {
            data.addAll(dataSnapshot)
        } else {
            for (item in dataSnapshot) {
                if (item.name.toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault()))) {
                    data.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }
}

class ContactsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val icon: ImageView = itemView.image_view
    private val name: TextView = itemView.text_view_name
    private val mobile: TextView = itemView.text_view_mobile

    fun bind(contact: Contact, clickListener: OnItemClickListener){
        name.text = contact.name
        mobile.text = contact.mobile
        val iconDrawable = TextDrawable.builder().buildRound(contact.name.first().toString(), generateColor(contact.name))
        icon.setImageDrawable(iconDrawable)

        itemView.setOnClickListener{
            clickListener.onItemClicked(contact)
        }
    }

    private fun generateColor(key: String): Int {
        val generator: ColorGenerator = ColorGenerator.MATERIAL
        return generator.getColor(key)
    }
}

interface OnItemClickListener{
    fun onItemClicked(contact: Contact)
}