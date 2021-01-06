package com.example.myagenda.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myagenda.R
import com.example.myagenda.adapters.AppointmentsAdapter
import com.example.myagenda.database.MyAgendaDatabaseHandler
import com.example.myagenda.fragments.AgendaListFragment
import com.example.myagenda.models.Appointment
import com.example.myagenda.models.Contact
import kotlinx.android.synthetic.main.activity_appointment.*
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.fragment_contact_detail.*
import kotlinx.android.synthetic.main.fragment_contact_detail.button_delete
import kotlinx.android.synthetic.main.fragment_contact_detail.button_save
import kotlinx.android.synthetic.main.fragment_list_agenda.*

class AppointmentActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        val dbHelper = MyAgendaDatabaseHandler(this)
        val appointment = intent.extras?.getParcelable<Appointment>("appointment")

        if (appointment == null) {
            // Creating new appointment
            button_delete.visibility = View.GONE

            val year = intent.extras?.getInt("year")
            val month = intent.extras?.getInt("month")
            val day = intent.extras?.getInt("day")

            button_save.setOnClickListener {
                val newAppointment = Appointment(
                        null,
                        year!!,
                        month!!,
                        day!!,
                        "hour",
                        text_input_layout_description.editText?.text.toString())

                val newAppointmentId = dbHelper.addAppointment(newAppointment)
                newAppointment.id = newAppointmentId
                dbHelper.addAppointment(newAppointment)
                onBackPressed()
            }
        } else {
            // Editing existing appointment
            text_input_layout_description.editText?.setText(appointment.description)

            button_save.setOnClickListener {
                val updatedAppointment = Appointment(
                        appointment.id,
                        appointment.year,
                        appointment.month,
                        appointment.day,
                        "hour",
                        text_input_layout_description.editText?.text.toString())

                dbHelper.updateAppointment(updatedAppointment)
                setResult(0)
                finish()
            }
        }

        button_delete.setOnClickListener {
            dbHelper.deleteAppointment(appointment?.id!!)
            setResult(0)
            finish()
        }
    }



    // TODO: hacer que esta activity pille el appointment y el resto de extras y haga lo que el fragment haria
    // TODO: manejar el cambio de orientacion en CalendarActivity pa poner uno al lado del otro
}