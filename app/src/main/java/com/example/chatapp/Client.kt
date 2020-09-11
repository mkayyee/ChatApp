package com.example.chatapp

import android.util.Log
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.util.*

// This class is the connection between the server and the client

class Client(input: InputStream, output: OutputStream) : Runnable {

    private val messageInput = Scanner(input)
    private val messageOutput = PrintStream(output, true)

    override fun run() {
        val printThread = Thread {
            while (true) {
                try {
                    if (Messages.messageToSend.isNotEmpty()) {
                        messageOutput.println(Messages.messageToSend)
                        Messages.messageToSend = ""
                    }
                } catch (e: Throwable) {
                    Log.d("Exception", "Client exception: $e")
                }
            }
        }
        printThread.start()

        val userNameThread = Thread {
            /* A username thread that runs until a username has been registered.
               This is to direct messages to their appropriate positions in the RecyclerView.
             */
            while (Messages.user.isEmpty()) {
                if (Messages.messageToSend.startsWith(":user") && Messages.messageToSend.split(" ").size > 1) {
                    val username = Messages.messageToSend.split(" ")[1]
                    Messages.user = username
                }
            }
        }
        userNameThread.start()

        while (true) {
            try {
                val received = messageInput.nextLine()
                Messages.addMessage(received)
            } catch (e: Exception) {
                Log.d("Exception", "Client exception: $e")
                break
            }
        }
    }
}

