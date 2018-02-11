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

/**
 * Main entry point for accessing tasks data.
 *
 *
 * For simplicity, only getCards() and getCard() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new task is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */
interface CardsDataSource {

    interface LoadCardsCallback {

        fun onCardsLoaded(tasks: List<Card>)

        fun onDataNotAvailable()
    }

    interface GetCardCallback {

        fun onCardLoaded(task: Card)

        fun onDataNotAvailable()
    }

    fun getCards(callback: LoadCardsCallback)

    fun getCard(taskId: String, callback: GetCardCallback)

    fun saveCard(task: Card)

    fun completeCard(task: Card)

    fun completeCard(taskId: String)

    fun activateCard(task: Card)

    fun activateCard(taskId: String)

    fun clearCompletedCards()

    fun refreshCards()

    fun deleteAllCards()

    fun deleteCard(taskId: String)
}
