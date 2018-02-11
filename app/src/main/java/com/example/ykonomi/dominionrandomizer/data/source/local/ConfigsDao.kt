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

package com.example.ykonomi.dominionrandomizer.data.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.example.ykonomi.dominionrandomizer.data.Config

/**
 * Data Access Object for the tasks table.
 */
@Dao interface ConfigsDao {

    /**
     * Select all tasks from the tasks table.
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM Configs") fun getConfigs(): List<Config>

    /**
     * Select a task by id.
     *
     * @param taskId the task id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM Configs WHERE entryid = :taskId") fun getConfigById(taskId: String): Config?

    /**
     * Insert a task in the database. If the task already exists, replace it.
     *
     * @param task the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertConfig(task: Config)

    /**
     * Update a task.
     *
     * @param task task to be updated
     * @return the number of tasks updated. This should always be 1.
     */
    @Update fun updateConfig(task: Config): Int

    /**
     * Update the complete status of a task
     *
     * @param taskId    id of the task
     * @param completed status to be updated
     */
    @Query("UPDATE configs SET completed = :completed WHERE entryid = :taskId")
    fun updateCompleted(taskId: String, completed: Boolean)

    /**
     * Delete a task by id.
     *
     * @return the number of tasks deleted. This should always be 1.
     */
    @Query("DELETE FROM Configs WHERE entryid = :taskId") fun deleteConfigById(taskId: String): Int

    /**
     * Delete all tasks.
     */
    @Query("DELETE FROM Configs") fun deleteConfigs()

    /**
     * Delete all completed tasks from the table.
     *
     * @return the number of tasks deleted.
     */
    @Query("DELETE FROM Configs WHERE completed = 1") fun deleteCompletedConfigs(): Int
}
