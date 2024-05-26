package org.chatapp

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.chatapp.firebaseauthentication.AndroidDatabaseDatabaseDriverFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Create instance of DriverFactory for Android
        val driverFactory = AndroidDatabaseDatabaseDriverFactory(this)
        setContent {
            App(driverFactory.createDriver())
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
}