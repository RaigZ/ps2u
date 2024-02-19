package com.example.ps2u.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import com.example.ps2u.R
import com.example.ps2u.WebSocket.ConnectionEvent
import com.example.ps2u.WebSocket.DetailEvent
import com.example.ps2u.WebSocket.FacilityControlEvent
import com.example.ps2u.WebSocket.Online
import com.example.ps2u.WebSocket.PS2WebSocketListener
import com.example.ps2u.WebSocket.Subscription
import com.example.ps2u.WebSocket.WebSocketListenerCallback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ServiceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ServiceFragment : Fragment(), WebSocketListenerCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var serviceStringBuilder: StringBuilder
    private lateinit var gson: Gson
    private lateinit var tvWSMessage : TextView
    private lateinit var svWSContent: ScrollView
    private lateinit var root: View

    private var ws: WebSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_service, container, false)
        tvWSMessage = root.findViewById(R.id.tvWSMessage)
        svWSContent = root.findViewById(R.id.svWSContent)
        gson = Gson()
        serviceStringBuilder = StringBuilder()
        return root
    }

    override fun onStart() {
        super.onStart()
        val cl = OkHttpClient()

        val request : Request = Request
            .Builder()
            .url("wss://push.planetside2.com/streaming?environment=ps2&service-id=s:example")
            .build()

        val listener = PS2WebSocketListener(this)
        ws = cl.newWebSocket(request, listener)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("onDestroy(): ", "Fragment destructor called")
    }

    override fun onStop() {
        super.onStop()
        ws?.close(1000, "Closure upon app stoppage")
        ws = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ServiceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ServiceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    @SuppressLint("SetTextI18n")
    override fun onMessageReceived(text: String) {
        Log.d("FRAGMENT RECEIVE: ", text)
        activity?.runOnUiThread {
            val jsonObject = gson.fromJson(text, Map::class.java) as Map<*, *>
            if(jsonObject.containsKey("connected")) {
                val connectionEvent = ConnectionEvent(jsonObject["connected"] ?: "", jsonObject["service"] ?: "", jsonObject["type"] ?: "")
                if(connectionEvent.connected.toString() == "true")
                    serviceStringBuilder.append("Connection Initialized.\n")
                else
                    serviceStringBuilder.append("Connection Failed.\n")
                tvWSMessage.post {
                    tvWSMessage.text = serviceStringBuilder.toString()
                    svWSContent.post {
                        svWSContent.fullScroll(ScrollView.FOCUS_DOWN)
                    }
                }
            } else if(jsonObject.containsKey("detail")) {
                val detailEvent = DetailEvent(jsonObject["detail"] ?: "", jsonObject["online"] ?: "", jsonObject["service"] ?: "", jsonObject["type"] ?: "")
                serviceStringBuilder.append("${detailEvent.detail} ")
                if (detailEvent.online.toString() == "true")
                    serviceStringBuilder.append("is online.\n")
                else
                    serviceStringBuilder.append("is offline.\n")
                tvWSMessage.post {
                    tvWSMessage.text = serviceStringBuilder.toString()
                    svWSContent.post {
                        svWSContent.fullScroll(ScrollView.FOCUS_DOWN)
                    }
                }
            } else if(jsonObject.containsKey("subscription")) {
                val subscription = gson.fromJson<Subscription>(jsonObject)
                val eventNamesList = subscription.subscription.eventNames
                val worldsList = subscription.subscription.worlds
                serviceStringBuilder.append("Subscribed events: ")
                iterateListFormat(eventNamesList, serviceStringBuilder)
                serviceStringBuilder.append("Subscribed worlds: ")
                iterateListFormat(worldsList, serviceStringBuilder)
                tvWSMessage.post {
                    tvWSMessage.text = serviceStringBuilder.toString()
                    svWSContent.post {
                        svWSContent.fullScroll(ScrollView.FOCUS_DOWN)
                    }
                }
            } else if(jsonObject.containsKey("online")) {
                val online = gson.fromJson<Online>(jsonObject)
                val eventServerStatus =
                    mapOf(
                        "Jaeger_19" to online.online.EventServerEndpoint_Jaeger_19,
                        "Cobalt_13" to online.online.EventServerEndpoint_Cobalt_13,
                        "Connery_1" to online.online.EventServerEndpoint_Connery_1,
                        "Emerald_17" to online.online.EventServerEndpoint_Emerald_17,
                        "Miller_10" to online.online.EventServerEndpoint_Miller_10,
                        "Soltech_40" to online.online.EventServerEndpoint_Soltech_40
                    )
                serviceStringBuilder.append("Status: ")
                eventServerStatus.forEach { (k, v) ->
                    if(v == "true")
                        serviceStringBuilder.append("${k}: active/")
                    else
                        serviceStringBuilder.append("${k}: inactive/")
                }
                serviceStringBuilder.append("timestamp: ${online.timestamp}/type: ${online.type}\n")

                tvWSMessage.post {
                    tvWSMessage.text = serviceStringBuilder.toString()
                    svWSContent.post {
                        svWSContent.fullScroll(ScrollView.FOCUS_DOWN)
                    }
                }
            } else if(jsonObject.containsKey("payload")) {
                val facilityControl = gson.fromJson<FacilityControlEvent>(jsonObject)
                var newFaction = ""
                var oldFaction = ""

                val factionId = mapOf(
                    "no faction" to "0",
                    "Vanu Sovereignty" to "1",
                    "New Conglomerate" to "2",
                    "Terran Republic" to "3"
                )

                factionId.forEach { (k, v) ->
                    when(v) {
                        facilityControl.payload.new_faction_id -> {
                            newFaction = k
                        }
                        facilityControl.payload.old_faction_id -> {
                            oldFaction = k
                        }
                    }
                }

                if(facilityControl.payload.new_faction_id == facilityControl.payload.old_faction_id) {
                    serviceStringBuilder.append(
                        "Facility ${facilityControl.payload.facility_id} defended by $newFaction.\n"
                        + "Contested for ${facilityControl.payload.duration_held} seconds.\n"
                    )

                } else {
                    serviceStringBuilder.append("Facility ${facilityControl.payload.facility_id} captured by $newFaction.\n")
                    serviceStringBuilder.append("Held for ${facilityControl.payload.duration_held} seconds by $oldFaction.\n")
                }
                tvWSMessage.post {
                    tvWSMessage.text = serviceStringBuilder.toString()
                    svWSContent.post {
                        svWSContent.fullScroll(ScrollView.FOCUS_DOWN)
                    }
                }
            }
            else {
                Log.e("WS:JSONParse", "Unrecognized format: $text")
            }
        }
    }
}

inline fun <reified T> Gson.fromJson(json: Map<*, *>): T {
    val jsonString = this.toJson(json)
    return this.fromJson(jsonString, object: TypeToken<T>() {}.type)
}

fun iterateListFormat(list: List<String>, stringBuilder: StringBuilder) {
    list.forEachIndexed { index, it ->
        if ((index < list.size - 1))
            stringBuilder.append("$it, ")
        else if(index >= list.size - 1)
            stringBuilder.append(it + '\n')
    }
}

