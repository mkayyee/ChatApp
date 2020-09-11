package com.example.chatapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.LinearLayout

class MessageAdapter(private val context: Context, private val mydata: MutableList<String>)
    :RecyclerView.Adapter<MessageAdapter.MyViewHolder>(){

    private val sender = 0
    private val receiver = 1

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val separateMessage = Messages.messageList[position]
            .substringAfter("${Messages.parseSender(Messages.messageList[position])}: ")
        // separates the message from the sender's name
        holder.mTextView.text = separateMessage
        holder.sTextView.text = Messages.parseSender(Messages.messageList[position])

    }
    override fun getItemCount(): Int {
        return mydata.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            /* Takes the linear layout - that contains TextViews for the sender and the actual message
                from their respective layout -> places it into a ViewHolder. */
        return if (viewType == 1) {
            val v = LayoutInflater.from(context).inflate(R.layout.received_message_layout,parent,false)
            MyViewHolder(v as LinearLayout)
        }else{
            val v = LayoutInflater.from(context).inflate(R.layout.sent_message_layout,parent,false)
            MyViewHolder(v as LinearLayout)
        }
    }
    override fun getItemViewType(position: Int): Int {
        val message = Messages.messageList[position]
        // Messages class's function for parsing sender out of a message
        Log.d("sender: ", Messages.parseSender(message))
        return if (Messages.checkSender(message)){
            sender
        }
        else
            receiver
    }
    open class MyViewHolder(v: LinearLayout) : RecyclerView.ViewHolder(v) {
        var mTextView: TextView = v.findViewById(R.id.text)
        var sTextView: TextView = v.findViewById(R.id.sender)

    }

}



