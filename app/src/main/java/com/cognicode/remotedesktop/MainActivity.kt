package com.cognicode.remotedesktop

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Base64
import android.webkit.JavascriptInterface
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class MainActivity : Activity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)
        setContentView(webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.settings.allowFileAccess = true
        webView.addJavascriptInterface(Bridge(), "AndroidBridge")
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                request.grant(request.resources)
            }
        }
        webView.loadUrl("file:///android_asset/viewer.html")
    }

    private inner class Bridge {
        @JavascriptInterface
        fun copy(text: String) {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("Device ID", text))
        }

        @JavascriptInterface
        fun turnCredential(): String {
            val username = (System.currentTimeMillis() / 1000 + 24 * 3600).toString()
            val mac = Mac.getInstance("HmacSHA1")
            mac.init(SecretKeySpec("openrelayprojectsecret".toByteArray(), "HmacSHA1"))
            val credential = Base64.encodeToString(mac.doFinal(username.toByteArray()), Base64.NO_WRAP)
            return "$username|$credential"
        }
    }
}
