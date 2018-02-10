package com.example.ykonomi.dominionrandomizer.utils

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Executor that runs a task on a new background thread.
 */
class DiskIOThreadExecutor : Executor {
    private val mDiskIO = Executors.newSingleThreadExecutor();

    override fun execute(command: Runnable) {
        mDiskIO.execute(command)
    }
}
