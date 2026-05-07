package io.github.virtualvance.aiinsightvault

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.multiplatform.webview.web.WebContent
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import io.github.virtualvance.aiinsightvault.database.InsightEntity
import io.github.virtualvance.aiinsightvault.database.VaultDao
import io.github.virtualvance.aiinsightvault.service.MockSummarizationService
import io.github.virtualvance.aiinsightvault.service.SummarizationService
import kotlin.time.Clock
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(vaultDao: VaultDao) {
    val webViewState = rememberWebViewState("https://gemini.google.com")
    val webViewNavigator = rememberWebViewNavigator()
    val coroutineScope = rememberCoroutineScope()
    val summarizationService: SummarizationService = remember { MockSummarizationService() }

    webViewState.webSettings.apply {
        isJavaScriptEnabled = true
        androidWebSettings.domStorageEnabled = true
    }

    // Slightly refined script: still grabs text, but cleans up extra whitespace
    val captureScript = "JSON.stringify(document.body.innerText.trim().substring(0, 500) + '...');"

    // --- SPRINT 4 UI STATES ---
    var showSaveDialog by remember { mutableStateOf(false) }
    var capturedData by remember { mutableStateOf("") }
    var isSummarizing by remember { mutableStateOf(false) }
    var summaryResult by remember { mutableStateOf<String?>(null) }
    var tagsResult by remember { mutableStateOf<List<String>>(emptyList()) }
    var insightTitle by remember { mutableStateOf("") }
    var newTagInput by remember { mutableStateOf("") }

    // State for the custom URL testing bar
    var urlInput by remember { mutableStateOf("https://gemini.google.com") }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(end = 8.dp)
                        ) {
                            Text("AI Vault", modifier = Modifier.padding(end = 8.dp))

                            // The Primary Gemini Button
                            Button(onClick = {
                                val geminiUrl = "https://gemini.google.com"
                                webViewState.content = WebContent.Url(geminiUrl)
                                urlInput = geminiUrl // Keeps the text bar in sync
                            }) {
                                Text("Gemini")
                            }

                            Spacer(Modifier.width(8.dp))

                            // The Custom URL Testing Bar
                            OutlinedTextField(
                                value = urlInput,
                                onValueChange = { urlInput = it },
                                modifier = Modifier.weight(1f).height(52.dp),
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium,
                                trailingIcon = {
                                    TextButton(onClick = {
                                        webViewState.content = WebContent.Url(urlInput)
                                    }) {
                                        Text("Go", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        // LAUNCH THE SCRIPT
                        webViewNavigator.evaluateJavaScript(captureScript) { result ->
                            // INSTEAD of Logcat, it now goes to the UI
                            val cleanResult = (result as? String)?.removeSurrounding("\"")?.replace("\\n", "\n") ?: "No data"
                            capturedData = cleanResult
                            insightTitle = "Insight ${Clock.System.now().toEpochMilliseconds()}" // Default title
                            showSaveDialog = true // Trigger the Dialog

                            // TRIGGER SUMMARIZATION
                            isSummarizing = true
                            summaryResult = null
                            tagsResult = emptyList()
                            coroutineScope.launch {
                                try {
                                    val resultObj = summarizationService.summarize(cleanResult)
                                    summaryResult = resultObj.summary
                                    tagsResult = resultObj.tags
                                } finally {
                                    isSummarizing = false
                                }
                            }
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text("Capture Insight")
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                WebView(
                    state = webViewState,
                    navigator = webViewNavigator,
                    modifier = Modifier.fillMaxSize()
                )

                // --- Sprint 4 Dialog ---
                if (showSaveDialog) {
                    AlertDialog(
                        onDismissRequest = { showSaveDialog = false },
                        title = { Text("Save to Vault") },
                        text = {
                            Column(modifier = Modifier.heightIn(max = 450.dp).verticalScroll(rememberScrollState())) {
                                Text("Secure Vault ID generated.", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelSmall)
                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = insightTitle,
                                    onValueChange = { insightTitle = it },
                                    label = { Text("Insight Title") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text("Raw Transcript Hook:", fontWeight = FontWeight.Bold)
                                Text(capturedData, style = MaterialTheme.typography.bodySmall)

                                Spacer(modifier = Modifier.height(16.dp))

                                // The LiteRT-LM Placeholder -> Dynamic Mock AI Area
                                Surface(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            "LiteRT-LM Processor",
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                        
                                        if (isSummarizing) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                "Status: Mock AI is processing...",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            LinearProgressIndicator(
                                                modifier = Modifier.fillMaxWidth(),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        } else {
                                            summaryResult?.let {
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    it,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                                
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text("Tags (Interactive):", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                                                
                                                // Interactive Tag Display
                                                FlowRow(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    tagsResult.forEach { tag ->
                                                        AssistChip(
                                                            onClick = { /* Could navigate to tag view later */ },
                                                            label = { Text(tag, style = MaterialTheme.typography.labelSmall) },
                                                            trailingIcon = {
                                                                IconButton(
                                                                    onClick = { tagsResult = tagsResult - tag },
                                                                    modifier = Modifier.size(16.dp)
                                                                ) {
                                                                    Icon(Icons.Default.Close, contentDescription = "Remove tag")
                                                                }
                                                            }
                                                        )
                                                    }
                                                }

                                                // Add Custom Tag Row
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                                                ) {
                                                    TextField(
                                                        value = newTagInput,
                                                        onValueChange = { newTagInput = it },
                                                        placeholder = { Text("New Tag", style = MaterialTheme.typography.labelSmall) },
                                                        modifier = Modifier.weight(1f).height(48.dp),
                                                        textStyle = MaterialTheme.typography.bodySmall,
                                                        singleLine = true
                                                    )
                                                    IconButton(onClick = {
                                                        if (newTagInput.isNotBlank()) {
                                                            tagsResult = tagsResult + newTagInput.trim()
                                                            newTagInput = ""
                                                        }
                                                    }) {
                                                        Icon(Icons.Default.Add, contentDescription = "Add tag")
                                                    }
                                                }
                                            } ?: Text(
                                                "Status: Awaiting local Gemma model integration...",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                        }
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        val newEntity = InsightEntity(
                                            title = insightTitle,
                                            rawContent = capturedData,
                                            summary = summaryResult,
                                            tags = tagsResult.joinToString(","),
                                            timestamp = kotlin.time.Clock.System.now().toEpochMilliseconds()
                                        )

                                        // 1. Shift to the background thread for the heavy database write
                                        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) {
                                            vaultDao.insertInsight(newEntity)
                                        }

                                        // 2. Safely close the UI dialog back on the Main Thread
                                        showSaveDialog = false
                                    }
                                },
                                enabled = !isSummarizing && insightTitle.isNotBlank()
                            ) {
                                Text("Confirm & Save")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showSaveDialog = false }) { Text("Cancel") }
                        }
                    )
                }
            }
        }
    }
}