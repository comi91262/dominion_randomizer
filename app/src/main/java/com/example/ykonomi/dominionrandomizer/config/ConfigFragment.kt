package com.example.ykonomi.dominionrandomizer.config

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch

import com.example.ykonomi.dominionrandomizer.R

class ConfigFragment : Fragment(), ConfigContract.View {

    override lateinit var presenter: ConfigContract.Presenter

    private lateinit var BasicSwitch : Switch
    private lateinit var IntrigueSwitch : Switch
    private lateinit var SeasideSwitch : Switch
    private lateinit var AlchemySwitch : Switch
    private lateinit var ProsperitySwitch : Switch
    private lateinit var CornucopiaSwitch : Switch
    private lateinit var HinterlandsSwitch : Switch
    private lateinit var DarkAgesSwitch : Switch
    private lateinit var GuildsSwitch : Switch
    private lateinit var AdventuresSwitch : Switch
    private lateinit var EmpiresSwitch : Switch
    private lateinit var Basic2ndSwitch : Switch
    private lateinit var Intrigue2ndSwitch : Switch
    private lateinit var NocturneSwitch : Switch

    override fun showSwitch() {
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.config_frag, container, false)

        // Set up switches views

        BasicSwitch = root.findViewById(R.id.switch_basic)
        IntrigueSwitch = root.findViewById(R.id.switch_intrigue)
        SeasideSwitch = root.findViewById(R.id.switch_seaside)
        AlchemySwitch = root.findViewById(R.id.switch_alchemy)
        ProsperitySwitch = root.findViewById(R.id.switch_prosperity)
        CornucopiaSwitch = root.findViewById(R.id.switch_cornucopia)
        HinterlandsSwitch = root.findViewById(R.id.switch_hinterlands)
        DarkAgesSwitch = root.findViewById(R.id.switch_dark_ages)
        GuildsSwitch = root.findViewById(R.id.switch_guilds)
        AdventuresSwitch = root.findViewById(R.id.switch_adventures)
        EmpiresSwitch = root.findViewById(R.id.switch_empires)
        Basic2ndSwitch = root.findViewById(R.id.switch_basic_2nd)
        Intrigue2ndSwitch = root.findViewById(R.id.switch_intrigue_2nd)
        NocturneSwitch = root.findViewById(R.id.switch_nocturne)


        BasicSwitch.setOnCheckedChangeListener { view, isChecked ->
            presenter.saveChecked(view.id, isChecked)
        }




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
