package com.example.corenetworking.localdata

import android.content.SharedPreferences
import io.reactivex.Single
import javax.inject.Inject

interface LocalPref {

    fun startSession(data: String): Single<String>
    fun endSession(): Single<Boolean>
    fun getSessionInfo(): Single<String>
}

class LocalPrefImpl @Inject constructor(private val pref: SharedPreferences) : LocalPref {

    override fun startSession(data: String): Single<String> {
        return Single.fromCallable {
            pref.edit().putString(SessionConstants.SESSION_START, data).apply()
            data
        }
    }

    override fun endSession(): Single<Boolean> {
        return Single.fromCallable { pref.edit().clear().commit() }
    }

    override fun getSessionInfo(): Single<String> {
        return Single.fromCallable { pref.getString(SessionConstants.SESSION_START, "") }
    }
}
