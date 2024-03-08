package delu.game.antivoronline.web

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.MutableFloat
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import delu.game.antivoronline.R
import delu.game.antivoronline.activity.WebViewActivity

class WebChromeClientAntivor(
    private val activity: ComponentActivity,
    private var progress: MutableFloatState
) : WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        progress.floatValue = newProgress.toFloat()
    }

    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        WebViewActivity.uploadMessage = filePathCallback

        val intent = fileChooserParams?.createIntent()
        try {
            (activity as WebViewActivity).resultLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            WebViewActivity.uploadMessage = null
            Toast.makeText(activity,
                activity.getString(R.string.cannot_open_file_chooser), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }


}