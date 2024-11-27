package com.thesua7.pokedex.core.extensions

import timber.log.Timber

object ThreadInfoLogger {
    fun logThreadInfo(tag: String) {
        val threadName = Thread.currentThread().name
        Timber.d("ThreadInfoLogger: $tag - Thread: $threadName")
    }
}