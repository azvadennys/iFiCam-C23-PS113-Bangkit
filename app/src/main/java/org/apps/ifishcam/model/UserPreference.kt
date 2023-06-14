package org.apps.ifishcam.model

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserPreference(context: Context) {

    companion object {
        private const val PREFS_NAME = "login_pref"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val USER_ID = "userId"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private lateinit var auth: FirebaseAuth

    fun setUserId(userId: String?) {
        val editor = preferences.edit()
        editor.putString("userId", userId)
        editor.apply()
    }

    fun setLogin(user: User) {
        val editor = preferences.edit()
        editor.putString(NAME, user.name)
        editor.putString(EMAIL, user.email)
        editor.putString(USER_ID, user.userId)
        editor.apply()
    }

    fun getUser(): User {
        auth = FirebaseAuth.getInstance()
        val name = preferences.getString(NAME, null)
        val email = preferences.getString(EMAIL, null)
        val userId = preferences.getString(USER_ID, null)
        return User(
            userId, name, email
        )
    }

    fun setLogout() {
        val editor = preferences.edit()
        editor.remove(getUser().userId)
        editor.clear()
        editor.apply()
    }


}