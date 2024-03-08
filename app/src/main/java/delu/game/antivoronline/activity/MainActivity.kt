package delu.game.antivoronline.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import delu.game.antivoronline.R
import delu.game.antivoronline.WebViewPage
import delu.game.antivoronline.ui.theme.AntivorOnlineTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Log.d(LOG_WEB_COOKIE, "requestPermissionAntivor finished")
        if (getSharedPreferences(SHARED_PREFERENCES_AUTHORIZATION, 0).contains(LOGIN)
            && getSharedPreferences(SHARED_PREFERENCES_AUTHORIZATION, 0).contains(PASSWORD)
        ) {
            Log.d(LOG_WEB_COOKIE, "intro intent")
//            startActivity(Intent(this@MainActivity, WebViewActivity::class.java))
            setContent{
                WebViewPage.SetWebView(
                    url = MainActivity.URL,
                    activity = this
                )
            }
        } else {
            Log.d(LOG_WEB_COOKIE, "after intent")
            setContent {
                AntivorOnlineTheme {
//                    Authorization(this)

                }
            }
        }

    }

    @Composable
    fun Authorization(context: ComponentActivity) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            val loginValue = remember { mutableStateOf(TextFieldValue()) }
            val passwordValue = remember { mutableStateOf(TextFieldValue()) }

//            Login Field
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.login))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                value = loginValue.value,
                onValueChange = {
                    loginValue.value = it
                }
            )

//            Password Field
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.password))
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                value = passwordValue.value,
                onValueChange = {
                    passwordValue.value = it
                }
            )

            Spacer(modifier = Modifier.padding(8.dp))

//            Button
            Button(
                onClick = {
                    context.getSharedPreferences(SHARED_PREFERENCES_AUTHORIZATION, 0)
                        .edit()
                        .putString(
                            LOGIN,
                            loginValue.value.text.trim()
                        ).putString(
                            PASSWORD,
                            passwordValue.value.text.trim()
                        ).apply()

                    startActivity(Intent(this@MainActivity, WebViewActivity::class.java))
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_200)),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 12.dp,
                    pressedElevation = 4.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text(
                    text = stringResource(R.string.register),
                    color = Color.Blue,
                    fontSize = 24.sp,
                )
            }
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

        var backEnabled: Boolean = false


        const val SHARED_PREFERENCES_AUTHORIZATION = "SHARED_PREFERENCES_AUTHORIZATION"
        const val LOGIN = "LOGIN"
        const val PASSWORD = "PASSWORD"
        const val NOTHING = "NOTHING"
    }
}





