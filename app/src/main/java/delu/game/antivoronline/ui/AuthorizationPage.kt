package delu.game.antivoronline.ui

import android.content.Intent
import androidx.activity.ComponentActivity
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
import delu.game.antivoronline.R
import delu.game.antivoronline.ui.activity.MainActivity
import delu.game.antivoronline.ui.activity.WebViewActivity

object AuthorizationPage {

    @Composable
    fun GetAuthorization(context: ComponentActivity) {

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
                    context.getSharedPreferences(MainActivity.SHARED_PREFERENCES_AUTHORIZATION, 0)
                        .edit()
                        .putString(
                            MainActivity.LOGIN,
                            loginValue.value.text.trim()
                        ).putString(
                            MainActivity.PASSWORD,
                            passwordValue.value.text.trim()
                        ).apply()

                    context.apply {
                        startActivity(Intent(context, WebViewActivity::class.java))
                        finish()
                    }

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
}