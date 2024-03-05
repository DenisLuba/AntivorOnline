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
        uploadMessage = filePathCallback

        val intent = fileChooserParams?.createIntent()
        try {
            resultLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            uploadMessage = null
            Toast.makeText(activity,
                activity.getString(R.string.cannot_open_file_chooser), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

//    ActivityResultLauncher<I> registerForActivityResult (
//                ActivityResultContract<I, O> contract,
//                ActivityResultCallback<O> callback)

//    ActivityResultContract определяет контракт: данные какого типа будут подаваться на вход и
//    какой тип будет представлять результат.

//    ActivityResultCallback представляет интерфейс с единственным методом onActivityResult(),
//    который определяет обработку полученного результата.

//    Метод registerForActivityResult() регистрирует функцию-колбек и возвращает объект
//    ActivityResultLauncher. С помощью этого мы можем запустить activity. Для этого у объекта
//    ActivityResultLauncher вызывается метод launch().

    private val resultLauncher: ActivityResultLauncher<Intent> =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                uploadMessage?.onReceiveValue(
                    FileChooserParams.parseResult(
                        result.resultCode,
                        intent
                    )
                )
                uploadMessage = null
            }
        }

//    ValueCallback - Интерфейс обратного вызова, используемый для асинхронного предоставления значений.
//    Метод ValueCallback-a onReceiveValue(value: T) вызывается, когда значение доступно.
        private var uploadMessage: ValueCallback<Array<Uri>>? = null

}