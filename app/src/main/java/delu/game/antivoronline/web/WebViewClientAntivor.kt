package delu.game.antivoronline.web

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.util.Log
import android.webkit.CookieManager
import android.webkit.HttpAuthHandler
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.compose.runtime.MutableState
import delu.game.antivoronline.ui.activity.AuthorizationActivity
import delu.game.antivoronline.ui.activity.MainActivity

class WebViewClientAntivor(
    private val activity: ComponentActivity,
    private var backEnabled: MutableState<Boolean>,
    private var visibility: MutableState<Boolean>
) : WebViewClient() {
    override fun onPageStarted(webView: WebView?, url: String?, favicon: Bitmap?) {
        Log.d(MainActivity.LOG_WEB, "onPageStarted")
        visibility.value = true
        backEnabled.value = webView?.canGoBack() ?: false
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        Log.d(MainActivity.LOG_WEB, "shouldOverrideUrlLoading")
        visibility.value = true
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onPageFinished(webView: WebView?, url: String?) {
        Log.d(MainActivity.LOG_WEB, "onPageFinished")
        visibility.value = false

        CookieManager.getInstance().apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(webView, true)
            flush()
            Log.d(MainActivity.LOG_WEB_COOKIE, "Cookie = ${getCookie(url)}")
        }
    }

    override fun onReceivedHttpAuthRequest(
        webView: WebView?,
        handler: HttpAuthHandler?,
        host: String?,
        realm: String?
    ) {
        countReceivedHttpAuthRequest++
        if (countReceivedHttpAuthRequest > 5) {
            webView?.stopLoading()
            countReceivedHttpAuthRequest = 0
            with(activity) {
                getSharedPreferences(MainActivity.SHARED_PREFERENCES_AUTHORIZATION, 0)
                    .edit()
                    .clear()
                    .apply()
                startActivity(Intent(activity, AuthorizationActivity::class.java)
                    .apply {
                        flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                    }
                )
                finish()
            }
        }

        val login = activity
            .getSharedPreferences(MainActivity.SHARED_PREFERENCES_AUTHORIZATION, 0)
            .getString(MainActivity.LOGIN, MainActivity.NOTHING)

        val password = activity
            .getSharedPreferences(MainActivity.SHARED_PREFERENCES_AUTHORIZATION, 0)
            .getString(MainActivity.PASSWORD, MainActivity.NOTHING)

        Log.d(MainActivity.LOG_WEB, "login = $login, password = $password")

        handler?.proceed(login, password)
    }

    @SuppressLint("WebViewClientOnReceivedSslError")
    override fun onReceivedSslError(
        view: WebView?,
        handler: SslErrorHandler?,
        error: SslError?
    ) {
        Log.e(MainActivity.LOG_WEB_COOKIE, "Error = ${error?.primaryError}")
        handler?.proceed()
    }

    companion object {
        private var countReceivedHttpAuthRequest = 0
    }
}