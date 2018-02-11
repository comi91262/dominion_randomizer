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
import android.support.annotation.VisibleForTesting
import com.example.ykonomi.dominionrandomizer.data.Card
import com.example.ykonomi.dominionrandomizer.data.source.CardsDataSource
import com.example.ykonomi.dominionrandomizer.utils.AppExecutors


/**
 * Concrete implementation of a data source as a db.
 */
class CardsLocalDataSource private constructor(
        val appExecutors: AppExecutors,
        val tasksDao: CardsDao
) : CardsDataSource {

    /**
     * Note: [CardsDataSource.LoadCardsCallback.onDataNotAvailable] is fired if the database doesn't exist
     * or the table is empty.
     */
    override fun getCards(callback: CardsDataSource.LoadCardsCallback) {
        appExecutors.diskIO.execute {
            val tasks = tasksDao.getCards()
            appExecutors.mainThread.execute {
                if (tasks.isEmpty()) {
                    // This will be called if the table is new or just empty.
                    callback.onDataNotAvailable()
                } else {
                    callback.onCardsLoaded(tasks)
                }
            }
        }
    }

    /**
     * Note: [CardsDataSource.GetCardCallback.onDataNotAvailable] is fired if the [Card] isn't
     * found.
     */
    override fun getCard(taskId: String, callback: CardsDataSource.GetCardCallback) {
        appExecutors.diskIO.execute {
            val task = tasksDao.getCardById(taskId)
            appExecutors.mainThread.execute {
                if (task != null) {
                    callback.onCardLoaded(task)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun saveCard(task: Card) {
        appExecutors.diskIO.execute { tasksDao.insertCard(task) }
    }

    override fun completeCard(task: Card) {
        appExecutors.diskIO.execute { tasksDao.updateCompleted(task.id, true) }
    }

    override fun completeCard(taskId: String) {
        // Not required for the local data source because the {@link CardsRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    override fun activateCard(task: Card) {
        appExecutors.diskIO.execute { tasksDao.updateCompleted(task.id, false) }
    }

    override fun activateCard(taskId: String) {
        // Not required for the local data source because the {@link CardsRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    override fun clearCompletedCards() {
        appExecutors.diskIO.execute { tasksDao.deleteCompletedCards() }
    }

    override fun refreshCards() {
        // Not required because the {@link CardsRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    override fun deleteAllCards() {
        appExecutors.diskIO.execute { tasksDao.deleteCards() }
    }

    override fun deleteCard(taskId: String) {
        appExecutors.diskIO.execute { tasksDao.deleteCardById(taskId) }
    }

    companion object {
        private var INSTANCE: CardsLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, tasksDao: CardsDao): CardsLocalDataSource {
            if (INSTANCE == null) {
                synchronized(CardsLocalDataSource::javaClass) {
                    INSTANCE = CardsLocalDataSource(appExecutors, tasksDao)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }
}
