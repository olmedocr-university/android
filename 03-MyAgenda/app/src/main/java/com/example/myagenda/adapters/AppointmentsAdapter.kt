package com.example.myagenda.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myagenda.R
import com.example.myagenda.models.Appointment
import kotlinx.android.synthetic.main.fragment_appointment.view.*

class AppointmentsAdapter(
        var data: ArrayList<Appointment>,
        val itemClickListener: OnAppointmentClickListener
): RecyclerView.Adapter<AppointmentsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_appointment,
                parent,
                false
        )
        return AppointmentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentsViewHolder, position: Int) {
        val appointment = data[position]
        holder.bind(appointment, itemClickListener)
    }

    override fun getItemCount(): Int = data.size

    fun onDataSetChange(data: ArrayList<Appointment>) {
        this.data = data
        this.notifyDataSetChanged()
    }
}

class AppointmentsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val date: TextView = itemView.text_view_date
    private val description: TextView = itemView.text_view_description

    fun bind(appointment: Appointment, clickListener: OnAppointmentClickListener){
        val text = appointment.year.toString() + appointment.month.toString() + appointment.day.toString()
        date.text = text
        description.text = appointment.description

        itemView.setOnClickListener{
            clickListener.onAppointmentClicked(appointment)
        }
    }
}

interface OnAppointmentClickListener {
    fun onAppointmentClicked(appointment: Appointment)
}