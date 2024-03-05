package delu.game.antivoronline

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

object WebView {

    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    fun SetWebView(
        url: String,
        activity: ComponentActivity,
        modifier: Modifier
    ) {
//  WebViewClient прослушивает onPageStarted, который проверяет, может ли WebView вернуться на страницу назад,
//  а затем обновляет backEnabled. Это вызывает перекомпоновку, которая включает и выключает BackHandler.
        val backEnabled: MutableState<Boolean> = remember { mutableStateOf(false) }
        val visibility = remember { mutableStateOf(true) }

    }


}