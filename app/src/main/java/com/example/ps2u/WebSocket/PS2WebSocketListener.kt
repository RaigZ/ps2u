package com.example.ps2u.WebSocket

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

interface WebSocketListenerCallback {
    fun onMessageReceived(text: String)
}

class PS2WebSocketListener(private val callback: WebSocketListenerCallback) : WebSocketListener() {
    private val NORMAL_CLOSURE_STATUS = 1000
//    private val
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        webSocket.send("{\"service\":\"event\",\"action\":\"subscribe\",\"worlds\":[\"1\",\"9\",\"10\",\"11\",\"13\",\"17\",\"18\",\"19\",\"25\",\"1000\",\"1001\"],\"eventNames\":[\"FacilityControl\",\"MetagameEvent\"]}")
        Log.d("WS:onopen", response.toString())
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d("WS:RECEIVED", text)
        callback.onMessageReceived(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        Log.d("WS:RECEIVED-BYTES", bytes.toString())
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.d("WS:onclosed", "WS connection closed")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        webSocket.close(NORMAL_CLOSURE_STATUS, reason)
        Log.d("WS:onclosing", "code: $code || reason: $reason")

    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.d("WS:onfailure", "t: $t || response: $response")
    }
}