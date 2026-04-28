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
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    // Setup the State and the Navigator (The Bridge Controller)
    val webViewState = rememberWebViewState("https://gemini.google.com")
    val webViewNavigator = rememberWebViewNavigator()

    webViewState.webSettings.apply {
        isJavaScriptEnabled = true
        androidWebSettings.domStorageEnabled = true
    }

    // --- THE CAPTURE SCRIPT ---
    // Use JSON.stringify to safely package the text so the bridge doesn't choke on raw line breaks.
    // This turns all the messy formatting into one solid, safe string of text.
    val captureScript = "JSON.stringify(document.body.innerText);"

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("AI Vault", modifier = Modifier.padding(end = 8.dp))

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
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        // LAUNCH THE SCRIPT
                        webViewNavigator.evaluateJavaScript(captureScript) { result ->
                            // COMBINE them into one string so the Logcat filter doesn't hide it!
                            println("BRIDGE RETURNED WITH DATA: $result")
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Text("Capture Insight")
                }
            }
        ) { paddingValues ->
            WebView(
                state = webViewState,
                navigator = webViewNavigator, // Attaches the controller to the portal
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    }
}