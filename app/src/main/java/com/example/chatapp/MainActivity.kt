package com.example.chatapp

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityService.SoftKeyboardController
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import java.io.PrintStream
import java.net.InetAddress
import java.net.Socket

class MainActivity : AppCompatActivity() {

    lateinit var s: Socket
    private val serverIp = InetAddress.getByName("10.0.2.2")
    private var messageCounter = Messages.messageList.size

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val outputText: EditText = findViewById(R.id.textOutput)
        val button: Button = findViewById(R.id.button)
        val receivedView = findViewById<RecyclerView>(R.id.recyclerReceived)

        receivedView.layoutManager = LinearLayoutManager(this)
        receivedView.adapter = MessageAdapter(this, Messages.messageList)
        // Now set the properties of the LinearLayoutManager

        fun clearAndSend() {
            /* this method stores EditText's (user input) content into a singleton variable
               that the Client class is listening to and clears EditText at the same time. */
            Messages.messageToSend = outputText.text.toString()
            outputText.text.clear()
        }
        button.setOnClickListener { clearAndSend() }

        val msgThread = Thread {
            /* A thread that scans for new messages for UI updating and scrolls recyclerView to the latest
               message received. If condition to prevent notifyDataSetChanged() for pointlessly
               wasting resources while no messages are being received. */
            while (true) {
                if (messageCounter < Messages.messageList.size) {
                    runOnUiThread { recyclerReceived.adapter?.notifyDataSetChanged() }
                    runOnUiThread { recyclerReceived.scrollToPosition(Messages.messageList.lastIndex) }
                    messageCounter = Messages.messageList.size
                }
            }
        }
        msgThread.start()

        val socketThread = Thread {
            /* The thread that initiates Socket (s) and starts
               a Client instance that communicates between the server and the app. */
            try {
                s = Socket(serverIp, 42420)
                val messageIn = s.getInputStream()
                val messageOut = PrintStream(s.getOutputStream(), true)
                val client = Client(messageIn, messageOut)
                client.run()
            } catch (e: Exception) {
                Log.d("Exception", "Socket exception: $e")
            }
        }
        socketThread.start()
    }
}