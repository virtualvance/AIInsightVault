package io.github.virtualvance.aiinsightvault

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform