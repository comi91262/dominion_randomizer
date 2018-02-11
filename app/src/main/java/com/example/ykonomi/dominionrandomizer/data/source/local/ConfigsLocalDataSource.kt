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
import com.example.ykonomi.dominionrandomizer.data.Config
import com.example.ykonomi.dominionrandomizer.data.source.ConfigsDataSource
import com.example.ykonomi.dominionrandomizer.utils.AppExecutors


/**
 * Concrete implementation of a data source as a db.
 */
class ConfigsLocalDataSource private constructor(
        val appExecutors: AppExecutors,
        val tasksDao: ConfigsDao
) : ConfigsDataSource {

    /**
     * Note: [ConfigsDataSource.LoadConfigsCallback.onDataNotAvailable] is fired if the database doesn't exist
     * or the table is empty.
     */
    override fun getConfigs(callback: ConfigsDataSource.LoadConfigsCallback) {
        appExecutors.diskIO.execute {
            val tasks = tasksDao.getConfigs()
            appExecutors.mainThread.execute {
                if (tasks.isEmpty()) {
                    // This will be called if the table is new or just empty.
                    callback.onDataNotAvailable()
                } else {
                    callback.onConfigsLoaded(tasks)
                }
            }
        }
    }

    /**
     * Note: [ConfigsDataSource.GetConfigCallback.onDataNotAvailable] is fired if the [Config] isn't
     * found.
     */
    override fun getConfig(taskId: String, callback: ConfigsDataSource.GetConfigCallback) {
        appExecutors.diskIO.execute {
            val task = tasksDao.getConfigById(taskId)
            appExecutors.mainThread.execute {
                if (task != null) {
                    callback.onConfigLoaded(task)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun saveConfig(task: Config) {
        appExecutors.diskIO.execute { tasksDao.insertConfig(task) }
    }

    override fun completeConfig(task: Config) {
        appExecutors.diskIO.execute { tasksDao.updateCompleted(task.id, true) }
    }

    override fun completeConfig(taskId: String) {
        // Not required for the local data source because the {@link ConfigsRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    override fun activateConfig(task: Config) {
        appExecutors.diskIO.execute { tasksDao.updateCompleted(task.id, false) }
    }

    override fun activateConfig(taskId: String) {
        // Not required for the local data source because the {@link ConfigsRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    override fun clearCompletedConfigs() {
        appExecutors.diskIO.execute { tasksDao.deleteCompletedConfigs() }
    }

    override fun refreshConfigs() {
        // Not required because the {@link ConfigsRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    override fun deleteAllConfigs() {
        appExecutors.diskIO.execute { tasksDao.deleteConfigs() }
    }

    override fun deleteConfig(taskId: String) {
        appExecutors.diskIO.execute { tasksDao.deleteConfigById(taskId) }
    }

    companion object {
        private var INSTANCE: ConfigsLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, tasksDao: ConfigsDao): ConfigsLocalDataSource {
            if (INSTANCE == null) {
                synchronized(ConfigsLocalDataSource::javaClass) {
                    INSTANCE = ConfigsLocalDataSource(appExecutors, tasksDao)
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
