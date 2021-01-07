package com.example.myagenda.activities

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.myagenda.R
import com.example.myagenda.adapters.OnAppointmentClickListener
import com.example.myagenda.fragments.AgendaListFragment
import com.example.myagenda.fragments.OnAddAppointmentButtonClickListener
import com.example.myagenda.models.Appointment
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.fragment_agenda.*
import java.util.*

class CalendarActivity : AppCompatActivity(), OnAppointmentClickListener, OnAddAppointmentButtonClickListener {

    var selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR)
    var selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    var selectedDay: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        if (savedInstanceState != null) {
            // Rotation

            selectedYear = savedInstanceState.getInt("year")
            selectedMonth = savedInstanceState.getInt("month")
            selectedDay = savedInstanceState.getInt("day")

            val calendar = Calendar.getInstance()
            calendar[Calendar.YEAR] = selectedYear
            calendar[Calendar.MONTH] = selectedMonth
            calendar[Calendar.DAY_OF_MONTH] = selectedDay

            calendarView.date = calendar.timeInMillis
        }

        setSupportActionBar(toolbar as Toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "My Appointments"

        (toolbar as Toolbar).setNavigationOnClickListener {
            onBackPressed()
        }

        calendarView.setOnDateChangeListener { _: CalendarView, i: Int, i1: Int, i2: Int ->
            selectedYear = i
            selectedMonth = i1
            selectedDay = i2

            (supportFragmentManager.findFragmentById(R.id.fragment_list) as AgendaListFragment).updateList(
                selectedYear,
                selectedMonth,
                selectedDay
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0) {
            if (resultCode == 0 ) {
                (supportFragmentManager.findFragmentById(R.id.fragment_list) as AgendaListFragment).updateList(
                    selectedYear,
                    selectedMonth,
                    selectedDay
                )
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putInt("year", selectedYear)
        outState.putInt("month", selectedMonth)
        outState.putInt("day", selectedDay)

        super.onSaveInstanceState(outState)
    }

    override fun onAppointmentClicked(appointment: Appointment) {
        goToDetail(appointment)
    }

    override fun onAddAppointmentButtonClicked() {
        goToDetail(null)
    }

    fun goToDetail(appointment: Appointment?){
        val intent = Intent(this, AppointmentActivity::class.java)
        if (appointment == null) {
            intent.putExtra("year", selectedYear)
            intent.putExtra("month", selectedMonth)
            intent.putExtra("day", selectedDay)
        } else {
            intent.putExtra("appointment", appointment)
        }

        startActivityForResult(intent, 0)
    }

}