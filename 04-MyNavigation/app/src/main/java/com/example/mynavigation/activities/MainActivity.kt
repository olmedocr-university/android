package com.example.mynavigation.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mynavigation.R
import com.example.mynavigation.adapters.OnItemClickListener
import com.example.mynavigation.fragments.ContactDetailFragment
import com.example.mynavigation.fragments.OnAddButtonClickListener
import com.example.mynavigation.models.Contact
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnItemClickListener, OnAddButtonClickListener {

    /* TODO:
        - Navigation Component: que es? Que soluciona?
        - Partes del navigation component: navigation graph, NavHostFragment y navigation controller
        - Como funcionan? Que hace cada cosa?
        - Como se implementa? (Esto hacerlo mientras se va explicando) y añado una pequeña comparación con fragmentManager
        - NavigationUI de toolbar
        - Opciones adicionales (rollo menu drawer y bottom navigation bar)
        - Demo
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = toolbar as Toolbar

        setSupportActionBar(toolbar)
        setupWithNavController(toolbar, navController, appBarConfiguration)

    }

    override fun onItemClicked(contact: Contact) {
        goToDetail(contact)
    }

    override fun onAddButtonClicked() {
        goToDetail(null)
    }

    fun goToList(view: View) {
        findNavController(view.id).popBackStack()
    }

    fun goToDetail(contact: Contact?) {

        val bundle = Bundle()
        bundle.putParcelable("contact", contact)
        findNavController(R.id.nav_host_fragment).navigate(R.id.action_contactListFragment_to_contactDetailFragment, bundle)
    }

}
