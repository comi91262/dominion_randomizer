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
package com.example.ykonomi.dominionrandomizer.data.source

import com.example.ykonomi.dominionrandomizer.data.Card
import java.util.ArrayList
import java.util.LinkedHashMap

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 *
 *
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
class CardsRepository(
//        val tasksRemoteDataSource: CardsDataSource,
        val tasksLocalDataSource: CardsDataSource
) : CardsDataSource {

    /**
     * This variable has public visibility so it can be accessed from tests.
     */
    var cachedCards: LinkedHashMap<String, Card> = LinkedHashMap()

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    var cacheIsDirty = false

    /**
     * Gets tasks from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     *
     *
     * Note: [CardsDataSource.LoadCardsCallback.onDataNotAvailable] is fired if all data sources fail to
     * get the data.
     */
    override fun getCards(callback: CardsDataSource.LoadCardsCallback) {
        // Respond immediately with cache if available and not dirty
        if (cachedCards.isNotEmpty() && !cacheIsDirty) {
            callback.onCardsLoaded(ArrayList(cachedCards.values))
            return
        }

        if (cacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getCardsFromRemoteDataSource(callback)
        } else {
            // Query the local storage if available. If not, query the network.
            tasksLocalDataSource.getCards(object : CardsDataSource.LoadCardsCallback {
                override fun onCardsLoaded(tasks: List<Card>) {
                    refreshCache(tasks)
                    callback.onCardsLoaded(ArrayList(cachedCards.values))
                }

                override fun onDataNotAvailable() {
                    getCardsFromRemoteDataSource(callback)
                }
            })
        }
    }

    override fun saveCard(task: Card) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
//            tasksRemoteDataSource.saveCard(it)
            tasksLocalDataSource.saveCard(it)
        }
    }

    override fun completeCard(task: Card) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
            it.isCompleted = true
//            tasksRemoteDataSource.completeCard(it)
            tasksLocalDataSource.completeCard(it)
        }
    }

    override fun completeCard(taskId: String) {
        getCardWithId(taskId)?.let {
            completeCard(it)
        }
    }

    override fun activateCard(task: Card) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
            it.isCompleted = false
//            tasksRemoteDataSource.activateCard(it)
            tasksLocalDataSource.activateCard(it)
        }
    }

    override fun activateCard(taskId: String) {
        getCardWithId(taskId)?.let {
            activateCard(it)
        }
    }

    override fun clearCompletedCards() {
//        tasksRemoteDataSource.clearCompletedCards()
        tasksLocalDataSource.clearCompletedCards()

        cachedCards = cachedCards.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, Card>
    }

    /**
     * Gets tasks from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     *
     *
     * Note: [CardsDataSource.GetCardCallback.onDataNotAvailable] is fired if both data sources fail to
     * get the data.
     */
    override fun getCard(taskId: String, callback: CardsDataSource.GetCardCallback) {
        val taskInCache = getCardWithId(taskId)

        // Respond immediately with cache if available
        if (taskInCache != null) {
            callback.onCardLoaded(taskInCache)
            return
        }

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        tasksLocalDataSource.getCard(taskId, object : CardsDataSource.GetCardCallback {
            override fun onCardLoaded(task: Card) {
                // Do in memory cache update to keep the app UI up to date
                cacheAndPerform(task) {
                    callback.onCardLoaded(it)
                }
            }

           override fun onDataNotAvailable() {
//                tasksRemoteDataSource.getCard(taskId, object : CardsDataSource.GetCardCallback {
//                    override fun onCardLoaded(task: Card) {
//                        // Do in memory cache update to keep the app UI up to date
//                        cacheAndPerform(task) {
//                            callback.onCardLoaded(it)
//                        }
//                    }
//
//                    override fun onDataNotAvailable() {
//                        callback.onDataNotAvailable()
//                    }
//                })
            }
        })
    }

    override fun refreshCards() {
        cacheIsDirty = true
    }

    override fun deleteAllCards() {
//        tasksRemoteDataSource.deleteAllCards()
        tasksLocalDataSource.deleteAllCards()
        cachedCards.clear()
    }

    override fun deleteCard(taskId: String) {
//        tasksRemoteDataSource.deleteCard(taskId)
        tasksLocalDataSource.deleteCard(taskId)
        cachedCards.remove(taskId)
    }

    private fun getCardsFromRemoteDataSource(callback: CardsDataSource.LoadCardsCallback) {
//        tasksRemoteDataSource.getCards(object : CardsDataSource.LoadCardsCallback {
//            override fun onCardsLoaded(tasks: List<Card>) {
//                refreshCache(tasks)
//                refreshLocalDataSource(tasks)
//                callback.onCardsLoaded(ArrayList(cachedCards.values))
//            }
//
//            override fun onDataNotAvailable() {
//                callback.onDataNotAvailable()
//            }
//        })
    }

    private fun refreshCache(tasks: List<Card>) {
        cachedCards.clear()
        tasks.forEach {
            cacheAndPerform(it) {}
        }
        cacheIsDirty = false
    }

    private fun refreshLocalDataSource(tasks: List<Card>) {
        tasksLocalDataSource.deleteAllCards()
        for (task in tasks) {
            tasksLocalDataSource.saveCard(task)
        }
    }

    private fun getCardWithId(id: String) = cachedCards[id]

    private inline fun cacheAndPerform(task: Card, perform: (Card) -> Unit) {
        val cachedCard = Card(task.title, task.description, task.id).apply {
            isCompleted = task.isCompleted
        }
        cachedCards.put(cachedCard.id, cachedCard)
        perform(cachedCard)
    }

    companion object {

        private var INSTANCE: CardsRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         * @param tasksRemoteDataSource the backend data source
         * *
         * @param tasksLocalDataSource  the device storage data source
         * *
         * @return the [CardsRepository] instance
         */
        @JvmStatic fun getInstance(//tasksRemoteDataSource: CardsDataSource,
                                   tasksLocalDataSource: CardsDataSource): CardsRepository {
            return INSTANCE ?: CardsRepository(tasksLocalDataSource)
                    .apply { INSTANCE = this }
        }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}
