package com.example.firebase_basedchatapplication

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase_basedchatapplication.databinding.ItemMessageBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    private val messages = mutableListOf<Message>()
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun setMessages(newMessages: List<Message>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount() = messages.size

    inner class MessageViewHolder(private val binding: ItemMessageBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            val context = binding.root.context
            val isSentByCurrentUser = message.senderId == currentUserId
            
            // Set message text
            binding.tvMessage.text = message.text
            
            // Format time
            val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
            val time = timeFormat.format(Date(message.timestamp))
            binding.tvTime.text = time
            
            // Configure message container layout
            val containerParams = binding.messageContainer.layoutParams as FrameLayout.LayoutParams
            
            if (isSentByCurrentUser) {
                // Sent message - iOS style (right-aligned, blue)
                containerParams.gravity = Gravity.END
                binding.messageCard.setBackgroundResource(R.drawable.ios_message_sent)
                
                // Text colors for sent messages (white on blue)
                binding.tvMessage.setTextColor(
                    ContextCompat.getColor(context, R.color.white)
                )
                binding.tvTime.setTextColor(
                    ContextCompat.getColor(context, R.color.white)
                )
                binding.tvTime.alpha = 0.7f
                
                // Hide sender name for sent messages
                binding.tvSender.visibility = View.GONE
                
            } else {
                // Received message - iOS style (left-aligned, gray)
                containerParams.gravity = Gravity.START
                binding.messageCard.setBackgroundResource(R.drawable.ios_message_received)
                
                // Text colors for received messages (black on gray)
                binding.tvMessage.setTextColor(
                    ContextCompat.getColor(context, R.color.text_primary)
                )
                binding.tvTime.setTextColor(
                    ContextCompat.getColor(context, R.color.text_tertiary)
                )
                binding.tvTime.alpha = 1f
                
                // Show sender name for received messages
                val senderName = if (message.senderEmail.isNotEmpty()) {
                    message.senderEmail.substringBefore("@")
                } else {
                    "Guest"
                }
                binding.tvSender.text = senderName
                binding.tvSender.visibility = View.VISIBLE
            }
            
            binding.messageContainer.layoutParams = containerParams
        }
    }
}
