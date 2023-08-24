package com.example.movieviewer.content

import android.text.TextUtils

/**
 * @author Marko Nikolic on 22.8.23.
 */
class AppErrorObject(private val errors: List<String>) {

    constructor(message: String) : this(listOf(message))

    fun joinAllForDisplay(): String = TextUtils.join("\n", errors.map { it })

    fun joinForThrowable(): Throwable = Throwable(joinAllForDisplay())
}