package io.realm.realmsurveys.extensions

import android.content.SharedPreferences
import io.realm.realmsurveys.BuildConfig
import java.util.*

fun SharedPreferences.uniqueUserId(): String {

    var idForCurrentUser = getString(BuildConfig.APPLICATION_ID, null)
    if (idForCurrentUser == null) {
        idForCurrentUser = UUID.randomUUID().toString()
        edit().putString(BuildConfig.APPLICATION_ID, idForCurrentUser).apply()
    }

    return idForCurrentUser
}
