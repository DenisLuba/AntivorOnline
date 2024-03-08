package delu.game.antivoronline.web

import android.content.ActivityNotFoundException
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.MutableFloatState
import delu.game.antivoronline.R
import delu.game.antivoronline.ui.activity.WebViewActivity

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