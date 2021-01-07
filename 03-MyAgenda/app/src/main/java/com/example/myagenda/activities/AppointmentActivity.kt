package com.example.myagenda.activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.myagenda.R
import com.example.myagenda.database.MyAgendaDatabaseHandler
import com.example.myagenda.models.Appointment
import kotlinx.android.synthetic.main.activity_appointment.*
import kotlinx.android.synthetic.main.activity_appointment.toolbar
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.fragment_agenda.*
import kotlinx.android.synthetic.main.fragment_contact_detail.button_delete
import kotlinx.android.synthetic.main.fragment_contact_detail.button_save

class AppointmentActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        val dbHelper = MyAgendaDatabaseHandler(this)
        val appointment = intent.extras?.getParcelable<Appointment>("appointment")

        setSupportActionBar(toolbar as Toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        (toolbar as Toolbar).setNavigationOnClickListener {
            onBackPressed()
        }

        if (appointment == null) {
            // Creating new appointment
            supportActionBar?.title = "New Appointment"
            button_delete.visibility = View.GONE

            val year = intent.extras?.getInt("year")
            val month = intent.extras?.getInt("month")
            val day = intent.extras?.getInt("day")

            button_save.setOnClickListener {
                val minute = time_picker_hour.minute
                val newTime: String

                newTime = if (minute <= 9) {
                    time_picker_hour.hour.toString() + ":0" + time_picker_hour.minute.toString()
                } else {
                    time_picker_hour.hour.toString() + ":" + time_picker_hour.minute.toString()
                }

                val newAppointment = Appointment(
                        null,
                        year!!,
                        month!!,
                        day!!,
                        newTime,
                        text_input_layout_description.editText?.text.toString())

                val newAppointmentId = dbHelper.addAppointment(newAppointment)
                newAppointment.id = newAppointmentId
                dbHelper.addAppointment(newAppointment)
                onBackPressed()
            }
        } else {
            // Editing existing appointment
            val time = appointment.hour
            val hour = time.substringBefore(":")
            val minute = time.substringAfter(":")
            text_input_layout_description.editText?.setText(appointment.description)
            time_picker_hour.hour = hour.toInt()
            time_picker_hour.minute = minute.toInt()

            supportActionBar?.title = appointment.description

            button_save.setOnClickListener {

                val minute = time_picker_hour.minute
                val newTime: String

                newTime = if (minute <= 9) {
                    time_picker_hour.hour.toString() + ":0" + time_picker_hour.minute.toString()
                } else {
                    time_picker_hour.hour.toString() + ":" + time_picker_hour.minute.toString()
                }


                val updatedAppointment = Appointment(
                        appointment.id,
                        appointment.year,
                        appointment.month,
                        appointment.day,
                        newTime,
                        text_input_layout_description.editText?.text.toString())

                dbHelper.updateAppointment(updatedAppointment)
                setResult(0)
                finish()
            }
        }

        if (savedInstanceState != null) {
            val hour = savedInstanceState.getInt("hour")
            val minute = savedInstanceState.getInt("minute")

            time_picker_hour.hour = hour
            time_picker_hour.minute = minute

            var debug = appointment

            Log.d("TAG", "onCreate: ")
        }

        button_delete.setOnClickListener {
            dbHelper.deleteAppointment(appointment?.id!!)
            setResult(0)
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onSaveInstanceState(outState: Bundle) {

        outState.putInt("hour", time_picker_hour.hour)
        outState.putInt("minute", time_picker_hour.minute)

        super.onSaveInstanceState(outState)
    }
}