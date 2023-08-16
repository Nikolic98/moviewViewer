package com.example.movieviewer.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * @author Marko Nikolic on 16.8.23.
 */
object PrefHelper : AppPrefHelper() {
    private const val TOKEN = "prefs:account_token"
    private var prefs: SharedPreferences? = null
    fun initPrefs(context: Context, name: String?, mode: Int) {
        prefs = context.getSharedPreferences(name, mode)
    }

    fun getPrefs(): SharedPreferences? {
        if (prefs != null) {
            return prefs
        }
        throw RuntimeException("Class not instantiated, call initPrefs() from onCreate()")
    }

    fun putString(key: String?, value: String?) {
        saveStringValue(getPrefs()!!, key!!, value!!)
    }

    fun getString(key: String?): String {
        return getStringValue(getPrefs()!!, key!!)
    }

    fun clearPrefs() {
        val editor = getPrefs()!!.edit()
        editor.clear()
        editor.apply()
    }

    fun saveToken(token: String?) {
        putString(TOKEN, token)
    }

    fun getToken() = getString(TOKEN)

    val isTokenSaved: Boolean
        get() = getToken().isNotEmpty()
}