package delu.game.antivoronline.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import delu.game.antivoronline.R
import delu.game.antivoronline.web.DownloadListenerAntivor
import delu.game.antivoronline.web.WebChromeClientAntivor
import delu.game.antivoronline.web.WebViewClientAntivor
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

object WebViewPage {

    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    fun SetWebView(
        url: String,
        activity: ComponentActivity
    ) {

        /**
        WebViewClient прослушивает onPageStarted, который проверяет, может ли WebView вернуться на страницу назад,
        а затем обновляет backEnabled. Это вызывает перекомпоновку, которая включает и выключает BackHandler.
         */
        val backEnabled: MutableState<Boolean> = remember { mutableStateOf(false) }
        val visibility = remember { mutableStateOf(true) }
        val progress = remember { mutableFloatStateOf(0.0f) }

        var webView: WebView? = null
        ProgressIndicator(visibility = visibility, percents = progress)

        AndroidView(
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewClientAntivor(activity, backEnabled, visibility)
                    webChromeClient = WebChromeClientAntivor(activity, progress)
                    setDownloadListener(DownloadListenerAntivor(activity))
                    setSettings()
                    loadUrl(url)
                    webView = this
                }
            }, update = {
                webView = it
            }
        )

        ProgressIndicator(visibility = visibility, percents = progress)
//        SignOutButton()
        BackHandler(enabled = backEnabled.value) {
            webView?.goBack()
        }
    }

    @Composable
    private fun ProgressIndicator(visibility: MutableState<Boolean>, percents: MutableFloatState) {
        if (!visibility.value) return

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
//                progress = percents.floatValue
            )
        }
    }

    @Composable
    private fun SignOutButton() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Image(painterResource(
                    id = R.drawable.sign_out),
                    contentDescription = "Sing out",
                    modifier = Modifier.size(28.dp)
                )
            }
        }

    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun WebView.setSettings() {
    with(settings) {
        // set up to support JS
        javaScriptEnabled = true
        // set up to support local storage (deprecated?)
        databaseEnabled = true
        // set up to support DomStorage
        domStorageEnabled = true
        // set the storage mode
        cacheMode = WebSettings.LOAD_DEFAULT
        // set fit to Screen
        useWideViewPort = true
        loadWithOverviewMode = true
        setSupportZoom(true)
        builtInZoomControls = true
        displayZoomControls = false
        requestFocus()

        allowFileAccess = true
        allowContentAccess = true
    }
}