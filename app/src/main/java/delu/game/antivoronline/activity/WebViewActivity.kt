package delu.game.antivoronline.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import delu.game.antivoronline.WebView
import delu.game.antivoronline.activity.ui.theme.AntivorOnlineTheme

class WebViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AntivorOnlineTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WebView.SetWebView(
                        url = MainActivity.URL,
                        activity = this,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
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

    val resultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                uploadMessage?.onReceiveValue(
                    WebChromeClient.FileChooserParams.parseResult(
                        result.resultCode,
                        intent
                    )
                )
                uploadMessage = null
            }
        }

    companion object {
        var uploadMessage: ValueCallback<Array<Uri>>? = null
    }
}
