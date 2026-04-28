package io.github.virtualvance.aiinsightvault

import android.webkit.JavascriptInterface

/**
 * The "Doorbell" class.
 * This acts as the bridge that allows JavaScript to send data back to the Kotlin code.
 */
class WebCaptureInterface(
    private val onContentReceived: (String) -> Unit
) {

    /**
     * This is the function that the JavaScript "bridge" will call.
     * The @JavascriptInterface annotation is a security gate; without it,
     * the web page is forbidden from seeing or touching this function.
     */
    @JavascriptInterface
    fun sendDataToAndroid(rawText: String) {
        // When data arrives, we pass it to the "onContentReceived" listener
        onContentReceived(rawText)
    }
}

