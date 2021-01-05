package com.example.myagenda.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.myagenda.R
import com.example.myagenda.activities.MainActivity

class AgendaFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agenda, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                AgendaFragment().apply {
                    arguments = Bundle().apply {}
                }
    }
}