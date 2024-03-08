package delu.game.antivoronline.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Log.d(LOG_WEB_COOKIE, "Start MainActivity")
        if (getSharedPreferences(SHARED_PREFERENCES_AUTHORIZATION, 0).contains(LOGIN)
            && getSharedPreferences(SHARED_PREFERENCES_AUTHORIZATION, 0).contains(PASSWORD)
            && getSharedPreferences(SHARED_PREFERENCES_AUTHORIZATION, 0).getString(LOGIN, NOTHING) != NOTHING
        ) {
            Log.d(LOG_WEB_COOKIE, "shared preferences is here")
            startActivity(Intent(this@MainActivity, WebViewActivity::class.java))
        } else {
            Log.d(LOG_WEB_COOKIE, "invoke Authorization")
            startActivity(Intent(this, AuthorizationActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            })
        }
        finish()
    }

    companion object {
        const val URL = "https://support.antivor.ru/"

        const val LOG_WEB: String = "LOG_WEB"
        const val LOG_WEB_COOKIE: String = "LOG_WEB_COOKIE"

        const val SHARED_PREFERENCES_AUTHORIZATION = "SHARED_PREFERENCES_AUTHORIZATION"
        const val LOGIN = "LOGIN"
        const val PASSWORD = "PASSWORD"
        const val NOTHING = "NOTHING"
    }
}





