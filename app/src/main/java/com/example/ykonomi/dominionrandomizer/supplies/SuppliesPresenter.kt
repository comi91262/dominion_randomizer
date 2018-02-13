package com.example.ykonomi.dominionrandomizer.config

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.ykonomi.dominionrandomizer.R
import com.example.ykonomi.dominionrandomizer.data.source.CardsRepository
import com.example.ykonomi.dominionrandomizer.supplies.SuppliesActivity

class SuppliesPresenter (
        val cardsRepository: CardsRepository,
        val suppliesActivity: SuppliesActivity,
        val suppliesView: SuppliesContract.View
) : SuppliesContract.Presenter {

    init {
        suppliesView.presenter = this
    }

    override fun start() {

    }

    override fun addCards() {
        val pref = suppliesActivity.getSharedPreferences("Set", MODE_PRIVATE)
        if (pref.getBoolean("@string/basic", false)){
            cardsRepository.getCard()

        }

    }

}
