package com.example.ps2u.WebSocket

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class PS2WebSocketListener : WebSocketListener() {
    private val NORMAL_CLOSURE_STATUS = 1000
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        webSocket.send("")
        Log.d("onopen", response.toString())
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d("RECEIVED: ", text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d("RECEIVED BYTES: ", bytes.toString())
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.d("onclosed", "code: $code || reason: $reason")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        webSocket.close(NORMAL_CLOSURE_STATUS, reason)
        Log.d("onclosing", "code: $code || reason: $reason")

    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.d("onfailure", "t: $t || response: $response")
    }
}