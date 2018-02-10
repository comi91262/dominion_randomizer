package com.example.ykonomi.dominionrandomizer.config

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.ykonomi.dominionrandomizer.R

class ConfigFragment : Fragment(), ConfigContract.View {

    override lateinit var presenter: ConfigContract.Presenter

    override fun showSwitch() {
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.config_frag, container, false)

        setHasOptionsMenu(true)
        //mDetailTitle = (TextView) root.findViewById(R.id.task_detail_title);
        //mDetailDescription = (TextView) root.findViewById(R.id.task_detail_description);
        //mDetailCompleteStatus = (CheckBox) root.findViewById(R.id.task_detail_complete);

        // Set up floating action button
        //FloatingActionButton fab =
        //        (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPresenter.editTask();
//            }
//        });

        return root
    }

    companion object {
        fun newInstance(): ConfigFragment {
            return ConfigFragment()
        }
    }

}// Required empty public constructor
