package com.example.ykonomi.dominionrandomizer.config

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.ykonomi.dominionrandomizer.R
import com.example.ykonomi.dominionrandomizer.data.source.ConfigsRepository

class ConfigPresenter (
        val configsActivity: Context,
        val ConfigsView: ConfigContract.View
) : ConfigContract.Presenter {

    init {
        ConfigsView.presenter = this
    }

    override fun start() {


    }

    override fun saveChecked(viewId: Int, isChecked: Boolean) {
        val pref = configsActivity.getSharedPreferences("Set", MODE_PRIVATE)
        when(viewId){
            R.id.switch_basic -> {
                pref.edit().putBoolean("@string/basic", isChecked).apply()
            }

        }
    }
}
