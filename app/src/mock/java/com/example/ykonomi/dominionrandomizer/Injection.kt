/*
 * Copyright (C) 2017 The Android Open Source Project
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
package com.example.ykonomi.dominionrandomizer
import android.content.Context
import com.example.ykonomi.dominionrandomizer.data.source.CardsRepository
import com.example.ykonomi.dominionrandomizer.data.source.ConfigsRepository
import com.example.ykonomi.dominionrandomizer.data.source.local.*
import com.example.ykonomi.dominionrandomizer.utils.AppExecutors

/**
 * Enables injection of mock implementations for
 * [CardsDataSource] at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
object Injection {
    fun provideCardsRepository(context: Context): CardsRepository {
        val database = CardsDatabase.getInstance(context)
        return CardsRepository.getInstance(//FakeCardsRemoteDataSource.getInstance(),
                CardsLocalDataSource.getInstance(AppExecutors(), database.taskDao()))
    }

    fun provideConfigsRepository(context: Context): ConfigsRepository {
        val database = ConfigsDatabase.getInstance(context)
        return ConfigsRepository.getInstance(//FakeConfigsRemoteDataSource.getInstance(),
                ConfigsLocalDataSource.getInstance(AppExecutors(), database.taskDao()))
    }
}
