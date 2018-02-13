package com.example.ykonomi.dominionrandomizer.config

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.ykonomi.dominionrandomizer.Injection
import com.example.ykonomi.dominionrandomizer.R
import com.example.ykonomi.dominionrandomizer.utils.replaceFragmentInActivity


class ConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.config_act)

        val configsFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
                as ConfigFragment? ?: ConfigFragment.newInstance().also {
            replaceFragmentInActivity(it, R.id.contentFrame)
        }

        ConfigPresenter(applicationContext, configsFragment)



    }
}
