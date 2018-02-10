package com.example.ykonomi.dominionrandomizer.config

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.ykonomi.dominionrandomizer.R
import com.example.ykonomi.dominionrandomizer.utils.addFragmentToActivity



class ConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.config_act)


        var cf = this.supportFragmentManager.findFragmentById(R.id.contentFrame)

        if (cf == null) {
            addFragmentToActivity(this.supportFragmentManager,
                    ConfigFragment.newInstance(), R.id.contentFrame)
        }

        //データベースへのアクセスクラスを渡す
        //ConfigPresenter(Injection.provideTasksRepository(getApplicationContext()), cf)

    }
}
