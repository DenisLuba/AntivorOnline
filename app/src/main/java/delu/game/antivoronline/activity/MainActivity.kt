package delu.game.antivoronline.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.HttpAuthHandler
import android.webkit.SslErrorHandler
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebViewDatabase
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    private lateinit var webViewDatabase: WebViewDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        webViewDatabase = WebViewDatabase.getInstance(this)
        setContent {
            val url = if (isInternetAvailable(LocalContext.current)) ANTIVOR_RU
            else URL_WITHOUT_INTERNET
            WebViewPage(url, webViewDatabase)
        }
    }
    companion object {
        const val URL = "https://support.antivor.ru/"

        const val ANTIVOR_RU = "https://support.antivor.ru/"
        const val METANIT_COM = "https://metanit.com"
        const val VK_RU = "https://vk.com/feed"
        const val URL_WITHOUT_INTERNET = "file:///android_asset/index.html"

        const val LOG_WEB_CLIENT: String = "LOG_WEB_CLIENT"
        const val LOG_WEB_COOKIE: String = "LOG_WEB_COOKIE"

        var backEnabled : Boolean = false



    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewPage(url: String, webViewDatabase: WebViewDatabase) {
//  WebViewClient прослушивает onPageStarted, который проверяет, может ли WebView вернуться на страницу назад,
//  а затем обновляет backEnabled. Это вызывает перекомпоновку, которая включает и выключает BackHandler.
    val backEnabled : MutableState<Boolean> = remember { mutableStateOf(false) }
    var webView: WebView? = null
    // Добавление WebView внутрь AndroidView в полноэкранном режиме
    AndroidView(
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                webViewClient = AntivorWebViewClient(backEnabled)

                // включаем JS
                settings.javaScriptEnabled = true
                // чтобы убедиться, что клиент, запрашивающий веб-страницу, на самом деле является нашим Android приложением
//            settings.userAgentString = System.getProperty("http.agent")
                loadUrl(url)
                webView = this
            }
        }, update = {
//        it.loadUrl(url)
            webView = it
        })

    BackHandler(enabled = backEnabled.value) {
        Log.d("MyLog", "backEnabled = ${backEnabled.value}")
        webView?.goBack()
    }
}

private fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

class AntivorWebViewClient(private var backEnabled : MutableState<Boolean>) : WebViewClient() {



    override fun onPageStarted(webView: WebView?, url: String?, favicon: Bitmap?) {
        backEnabled.value = webView?.canGoBack() ?: false
    }

    override fun onReceivedHttpAuthRequest(
        view: WebView?,
        handler: HttpAuthHandler?,
        host: String?,
        realm: String?
    ) {
//        handler?.proceed("pershin", "Zxlalala86351.")
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

