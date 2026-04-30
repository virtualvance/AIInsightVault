package io.github.virtualvance.aiinsightvault.service

import kotlinx.coroutines.delay

data class SummarizationResult(
    val summary: String,
    val tags: List<String>
)

interface SummarizationService {
    suspend fun summarize(text: String): SummarizationResult
}

class MockSummarizationService : SummarizationService {
    override suspend fun summarize(text: String): SummarizationResult {
        // Simulate local AI processing delay
        delay(2000)
        
        return SummarizationResult(
            summary = "This is a mock summary of the captured insight. It highlights the key points of the conversation regarding local AI integration and agnostic persistence.",
            tags = listOf("Mock", "AI", "Sprint4", "Kotlin")
        )
    }
}
