package com.example.chatapp
import android.util.Log

object Messages {

    var messageToSend = ""
    var user = ""
    val messageList = mutableListOf<String>()
    private var sender = "Server"

    fun checkSender(msg: String): Boolean {
        return if (user.isNotEmpty()) {
            msg.substringBefore(":") == user
        }
        else
            false
    }
    fun parseSender(message: String) : String {
        if (message[0].toString() != "["){
            if(message.split(" ")[0].contains(":") && message.split(" ")[0].substringBefore(":").isNotEmpty()){
                sender = message.substringBefore(":")
                return sender
                }
            else
                sender = "Server"
        }
        else
            sender = "Server"
        return sender

    }
    fun addMessage(message: String) {
        Log.d("adding message: ", message)
        messageList.add(message)
    }
}