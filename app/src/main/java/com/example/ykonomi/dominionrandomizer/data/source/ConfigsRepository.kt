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

import com.example.ykonomi.dominionrandomizer.data.Config
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
class ConfigsRepository(
//        val tasksRemoteDataSource: ConfigsDataSource,
        val tasksLocalDataSource: ConfigsDataSource
) : ConfigsDataSource {

    /**
     * This variable has public visibility so it can be accessed from tests.
     */
    var cachedConfigs: LinkedHashMap<String, Config> = LinkedHashMap()

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
     * Note: [ConfigsDataSource.LoadConfigsCallback.onDataNotAvailable] is fired if all data sources fail to
     * get the data.
     */
    override fun getConfigs(callback: ConfigsDataSource.LoadConfigsCallback) {
        // Respond immediately with cache if available and not dirty
        if (cachedConfigs.isNotEmpty() && !cacheIsDirty) {
            callback.onConfigsLoaded(ArrayList(cachedConfigs.values))
            return
        }

        if (cacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getConfigsFromRemoteDataSource(callback)
        } else {
            // Query the local storage if available. If not, query the network.
            tasksLocalDataSource.getConfigs(object : ConfigsDataSource.LoadConfigsCallback {
                override fun onConfigsLoaded(tasks: List<Config>) {
                    refreshCache(tasks)
                    callback.onConfigsLoaded(ArrayList(cachedConfigs.values))
                }

                override fun onDataNotAvailable() {
                    getConfigsFromRemoteDataSource(callback)
                }
            })
        }
    }

    override fun saveConfig(task: Config) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
//            tasksRemoteDataSource.saveConfig(it)
            tasksLocalDataSource.saveConfig(it)
        }
    }

    override fun completeConfig(task: Config) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
            it.isCompleted = true
//            tasksRemoteDataSource.completeConfig(it)
            tasksLocalDataSource.completeConfig(it)
        }
    }

    override fun completeConfig(taskId: String) {
        getConfigWithId(taskId)?.let {
            completeConfig(it)
        }
    }

    override fun activateConfig(task: Config) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
            it.isCompleted = false
//            tasksRemoteDataSource.activateConfig(it)
            tasksLocalDataSource.activateConfig(it)
        }
    }

    override fun activateConfig(taskId: String) {
        getConfigWithId(taskId)?.let {
            activateConfig(it)
        }
    }

    override fun clearCompletedConfigs() {
//        tasksRemoteDataSource.clearCompletedConfigs()
        tasksLocalDataSource.clearCompletedConfigs()

        cachedConfigs = cachedConfigs.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, Config>
    }

    /**
     * Gets tasks from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     *
     *
     * Note: [ConfigsDataSource.GetConfigCallback.onDataNotAvailable] is fired if both data sources fail to
     * get the data.
     */
    override fun getConfig(taskId: String, callback: ConfigsDataSource.GetConfigCallback) {
        val taskInCache = getConfigWithId(taskId)

        // Respond immediately with cache if available
        if (taskInCache != null) {
            callback.onConfigLoaded(taskInCache)
            return
        }

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        tasksLocalDataSource.getConfig(taskId, object : ConfigsDataSource.GetConfigCallback {
            override fun onConfigLoaded(task: Config) {
                // Do in memory cache update to keep the app UI up to date
                cacheAndPerform(task) {
                    callback.onConfigLoaded(it)
                }
            }

            override fun onDataNotAvailable() {
 //               tasksRemoteDataSource.getConfig(taskId, object : ConfigsDataSource.GetConfigCallback {
//                    override fun onConfigLoaded(task: Config) {
//                        // Do in memory cache update to keep the app UI up to date
//                        cacheAndPerform(task) {
//                            callback.onConfigLoaded(it)
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

    override fun refreshConfigs() {
        cacheIsDirty = true
    }

    override fun deleteAllConfigs() {
//        tasksRemoteDataSource.deleteAllConfigs()
        tasksLocalDataSource.deleteAllConfigs()
        cachedConfigs.clear()
    }

    override fun deleteConfig(taskId: String) {
//        tasksRemoteDataSource.deleteConfig(taskId)
        tasksLocalDataSource.deleteConfig(taskId)
        cachedConfigs.remove(taskId)
    }

    private fun getConfigsFromRemoteDataSource(callback: ConfigsDataSource.LoadConfigsCallback) {
//        tasksRemoteDataSource.getConfigs(object : ConfigsDataSource.LoadConfigsCallback {
//            override fun onConfigsLoaded(tasks: List<Config>) {
//                refreshCache(tasks)
//                refreshLocalDataSource(tasks)
//                callback.onConfigsLoaded(ArrayList(cachedConfigs.values))
//            }
//
//            override fun onDataNotAvailable() {
//                callback.onDataNotAvailable()
//            }
//        })
    }

    private fun refreshCache(tasks: List<Config>) {
        cachedConfigs.clear()
        tasks.forEach {
            cacheAndPerform(it) {}
        }
        cacheIsDirty = false
    }

    private fun refreshLocalDataSource(tasks: List<Config>) {
        tasksLocalDataSource.deleteAllConfigs()
        for (task in tasks) {
            tasksLocalDataSource.saveConfig(task)
        }
    }

    private fun getConfigWithId(id: String) = cachedConfigs[id]

    private inline fun cacheAndPerform(task: Config, perform: (Config) -> Unit) {
        val cachedConfig = Config(task.title, task.description, task.id).apply {
            isCompleted = task.isCompleted
        }
        cachedConfigs.put(cachedConfig.id, cachedConfig)
        perform(cachedConfig)
    }

    companion object {

        private var INSTANCE: ConfigsRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         * @param tasksRemoteDataSource the backend data source
         * *
         * @param tasksLocalDataSource  the device storage data source
         * *
         * @return the [ConfigsRepository] instance
         */
        @JvmStatic fun getInstance(//tasksRemoteDataSource: ConfigsDataSource,
                                   tasksLocalDataSource: ConfigsDataSource): ConfigsRepository {
            return INSTANCE ?: ConfigsRepository(tasksLocalDataSource)
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
