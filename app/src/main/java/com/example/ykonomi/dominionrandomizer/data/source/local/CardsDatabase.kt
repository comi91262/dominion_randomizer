/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ykonomi.dominionrandomizer.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.ykonomi.dominionrandomizer.data.Card

/**
 * The Room Database that contains the Card table.
 */
@Database(entities = arrayOf(Card::class), version = 1)
abstract class CardsDatabase : RoomDatabase() {

    abstract fun taskDao(): CardsDao

    companion object {

        private var INSTANCE: CardsDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): CardsDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            CardsDatabase::class.java, "Cards.db")
                            .build()
                }
                return INSTANCE!!
            }
        }
    }

}
