package com.example.myagenda.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myagenda.R
import com.example.myagenda.activities.CalendarActivity
import com.example.myagenda.adapters.AppointmentsAdapter
import com.example.myagenda.database.MyAgendaDatabaseHandler
import kotlinx.android.synthetic.main.fragment_list_agenda.*
import kotlinx.android.synthetic.main.fragment_list_agenda.view.*
import java.util.*

class AgendaListFragment : Fragment() {
    private var columnCount = 1

    var defaultYear: Int = Calendar.getInstance().get(Calendar.YEAR)
    var defaultMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    var defaultDay: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_agenda, container, false)
        val dbHandler = MyAgendaDatabaseHandler(activity!!.applicationContext)

        with(view) {
            // Set the adapter
            recycler_appointment.layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            recycler_appointment.adapter = AppointmentsAdapter(dbHandler.getDayAppointments(defaultYear, defaultMonth, defaultDay), activity as CalendarActivity)

            // Set the FAB
            fab_add.setOnClickListener {
                (activity as CalendarActivity).onAddAppointmentButtonClicked()
            }

        }

        return view
    }

    fun updateList(newYear: Int, newMonth: Int, newDay: Int) {
        val dbHandler = MyAgendaDatabaseHandler(activity!!.applicationContext)
        val newAppointments = dbHandler.getDayAppointments(newYear, newMonth, newDay)

        (recycler_appointment.adapter as AppointmentsAdapter).onDataSetChange(newAppointments)
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

interface OnAddAppointmentButtonClickListener {
    fun onAddAppointmentButtonClicked()
}