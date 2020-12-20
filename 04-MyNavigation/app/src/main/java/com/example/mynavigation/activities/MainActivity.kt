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

    // TODO: handle orientation change in detail fragment
    // TODO: no snackbars shown
    // TODO: unable to import nor export
    // TODO: put two fragments side by side on portrait in list fragment
    // TODO: call action not working
    // TODO: add modify or delete an appointment

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
