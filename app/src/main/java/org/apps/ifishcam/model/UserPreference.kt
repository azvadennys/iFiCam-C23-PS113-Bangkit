package org.apps.ifishcam.model

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserPreference(context: Context) {

    companion object {
        private const val PREFS_NAME = "login_pref"
        private const val NAME = "name"
        private const val USER_ID = "userId"
        private const val PHOTO_URL = "photoUrl"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private lateinit var auth: FirebaseAuth

    fun setLogin(user: User) {
        val editor = preferences.edit()
        editor.putString(NAME, user.name)
        editor.putString(USER_ID, user.userId)
        editor.putString(PHOTO_URL, user.photoUrl.toString())
        editor.apply()
    }

    fun getUser(): User {
        auth = FirebaseAuth.getInstance()
        val name = preferences.getString(NAME, null)
        val userId = preferences.getString(USER_ID, null)
        val photoUrl = preferences.getString(PHOTO_URL, null)
        return User(
            userId, name, photoUrl
        )
    }

    fun setLogout() {
        val editor = preferences.edit().clear()
        editor.apply()
    }


}