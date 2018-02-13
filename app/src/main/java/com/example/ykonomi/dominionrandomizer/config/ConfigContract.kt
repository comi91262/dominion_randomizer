package com.example.ykonomi.dominionrandomizer.config

import com.example.ykonomi.dominionrandomizer.BasePresenter
import com.example.ykonomi.dominionrandomizer.utils.BaseView

interface ConfigContract  {

    interface View : BaseView<Presenter> {
        fun showSwitch()
    }

    interface Presenter : BasePresenter {
        fun saveChecked(viewId: Int, isChecked: Boolean)
    }

}

