package com.example.ykonomi.dominionrandomizer.config

import com.example.ykonomi.dominionrandomizer.data.source.ConfigsRepository

class ConfigPresenter (
        val configsRepository: ConfigsRepository,
        val ConfigsView: ConfigContract.View
) : ConfigContract.Presenter {

    init {
        ConfigsView.presenter = this
    }

    override fun start() {
    }

    override fun getData() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
