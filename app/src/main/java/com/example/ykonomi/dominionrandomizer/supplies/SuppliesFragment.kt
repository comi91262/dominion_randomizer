package com.example.ykonomi.dominionrandomizer.supplies

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

import com.example.ykonomi.dominionrandomizer.R

class SuppliesFragment : Fragment(), SuppliesContract.View {

    override lateinit var presenter: SuppliesContract.Presenter
    private lateinit var adapter: ArrayAdapter<String>

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val root = inflater.inflate(R.layout.supplies_frag, container, false)
        val listView = root.findViewById<ListView>(R.id.tasks_list)

        adapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1)

        listView.adapter = adapter

        // Set up floating action button
        activity!!.findViewById<FloatingActionButton>(R.id.fab_add_task).apply {
            setImageResource(R.drawable.ic_add)
            setOnClickListener { presenter.addCards() }
        }

        return root
    }

    override fun showAddCard() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun newInstance() = SuppliesFragment()
    }

}// Required empty public constructor
