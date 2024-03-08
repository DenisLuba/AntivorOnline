package delu.game.antivoronline.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import delu.game.antivoronline.WebViewPage
import delu.game.antivoronline.activity.ui.theme.AntivorOnlineTheme

class WebViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestPermissionsAntivor()
        setContent {
            AntivorOnlineTheme {
                WebViewPage.SetWebView(
                    url = MainActivity.URL,
                    activity = this
                )
            }
        }
    }

    private fun requestPermissionsAntivor() {
        val permissionsList = mutableListOf<String>(
            Manifest.permission.CAMERA
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // SDK 33
            permissionsList.add(Manifest.permission.READ_MEDIA_IMAGES)
            permissionsList.add(Manifest.permission.READ_MEDIA_VIDEO)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) // SDK 34
                permissionsList.add(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
        } else permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permissionsList.all { !checkOnePermission(it) })
            ActivityCompat.requestPermissions(
                this,
                permissionsList.filter { !checkOnePermission(it) }.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
    }

    private fun checkOnePermission(permission: String) =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    /**
    ActivityResultLauncher<I> registerForActivityResult (
    ActivityResultContract<I, O> contract,
    ActivityResultCallback<O> callback)

    ActivityResultContract определяет контракт: данные какого типа будут подаваться на вход и
    какой тип будет представлять результат.

    ActivityResultCallback представляет интерфейс с единственным методом onActivityResult(),
    который определяет обработку полученного результата.

    Метод registerForActivityResult() регистрирует функцию-колбек и возвращает объект
    ActivityResultLauncher. С помощью этого мы можем запустить activity. Для этого у объекта
    ActivityResultLauncher вызывается метод launch().
     */
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
        /**
        ValueCallback - Интерфейс обратного вызова, используемый для асинхронного предоставления значений.
        Метод ValueCallback-a onReceiveValue(value: T) вызывается, когда значение доступно.
         */
        var uploadMessage: ValueCallback<Array<Uri>>? = null

        const val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    }
}
