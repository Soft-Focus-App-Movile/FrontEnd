package com.softfocus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.softfocus.core.navigation.AppNavigation
import com.softfocus.core.ui.theme.SoftFocusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SoftFocusTheme {
                AppNavigation()
            }
        }
    }
}
