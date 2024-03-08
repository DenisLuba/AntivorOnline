package delu.game.antivoronline.web

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.util.Log
import android.webkit.CookieManager
import android.webkit.HttpAuthHandler
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.MutableState
import delu.game.antivoronline.activity.MainActivity

class WebViewClientAntivor(
    private val context: Context,
    private var backEnabled: MutableState<Boolean>,
    private var visibility: MutableState<Boolean>
) : WebViewClient() {
    override fun onPageStarted(webView: WebView?, url: String?, favicon: Bitmap?) {
        Log.d(MainActivity.LOG_WEB_CLIENT, "onPageStarted")
        visibility.value = true
        backEnabled.value = webView?.canGoBack() ?: false
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        Log.d(MainActivity.LOG_WEB_CLIENT, "shouldOverrideUrlLoading")
        visibility.value = true
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onPageFinished(webView: WebView?, url: String?) {
        Log.d(MainActivity.LOG_WEB_CLIENT, "onPageFinished")
        visibility.value = false
        CookieManager.getInstance().apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(webView, true)
            flush()
            Log.d(MainActivity.LOG_WEB_COOKIE, "Cookie = ${getCookie(url)}")
        }
    }

    override fun onReceivedHttpAuthRequest(
        view: WebView?,
        handler: HttpAuthHandler?,
        host: String?,
        realm: String?
    ) {
        val login = context
            .getSharedPreferences(MainActivity.SHARED_PREFERENCES_AUTHORIZATION, 0)
            .getString(MainActivity.LOGIN, MainActivity.NOTHING)

        val password = context
            .getSharedPreferences(MainActivity.SHARED_PREFERENCES_AUTHORIZATION, 0)
            .getString(MainActivity.PASSWORD, MainActivity.NOTHING)

        Log.d(MainActivity.LOG_WEB_CLIENT, "login = $login, password = $password")

        handler?.proceed(login, password)
    }

    @SuppressLint("WebViewClientOnReceivedSslError")
    override fun onReceivedSslError(
        view: WebView?,
        handler: SslErrorHandler?,
        error: SslError?
    ) {
        handler?.proceed()
    }
}