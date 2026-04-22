package io.github.virtualvance.aiinsightvault

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.multiplatform.webview.web.WebContent
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    // Initialize the state with a default URL
    val webViewState = rememberWebViewState("https://gemini.google.com")

    // Configure Multiplatform Settings
    // This ensures JS and Storage work on both Android and later on an iPhone
    webViewState.webSettings.apply {
        isJavaScriptEnabled = true // Enable JS
        androidWebSettings.domStorageEnabled = true // Keep logged in and also cookies
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("AI Vault", modifier = Modifier.padding(end = 8.dp))

                            // Navigation Buttons
                            Button(onClick = { webViewState.content = WebContent.Url("https://gemini.google.com") }) {
                                Text("Gemini")
                            }
                            Spacer(Modifier.width(4.dp))
                            Button(onClick = { webViewState.content = WebContent.Url("https://chatgpt.com") }) {
                                Text("GPT")
                            }
                            Spacer(Modifier.width(4.dp))
                            Button(onClick = { webViewState.content = WebContent.Url("https://claude.ai") }) {
                                Text("Claude")
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            // WebView portal
            WebView(
                state = webViewState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    }
}