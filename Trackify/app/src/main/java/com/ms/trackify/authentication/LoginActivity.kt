package com.ms.trackify.authentication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.ms.apptheme.ui.theme.AppTheme
import com.ms.trackify.MainActivity
import com.ms.trackify.Navigation.AuthNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                AuthNavigation()
            }
        }
    }
}

/**
 * From Main Activity to login activity
 * Explicit Intent
 *
 * Intent(applicationContext, LoginActivity::java.class).also {
 *      startActivity(it)
 * }
 *
 * Open the youtube app or some other app
 *
 * Intent(Intent.ACTION_MAIN).also {
 *  it.`package` = "com.google.android.youtube"
 *  try {
 *      startActivity(it)
 *  } catch(e: ActivityNotFoundException) {
 *      e.printStackTrace()
 *  }
 * }
 *
 * */


