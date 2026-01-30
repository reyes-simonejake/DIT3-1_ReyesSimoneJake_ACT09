package com.example.firebase_basedchatapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebase_basedchatapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        if (auth.currentUser == null) {
            navigateToLogin()
            return
        }

        setupToolbar()
        setupRecyclerView()
        setupSendButton()
        loadMessages()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.chat_room)
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = messageAdapter
        }
    }

    private fun setupSendButton() {
        binding.btnSend.setOnClickListener {
            val messageText = binding.etMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
            }
        }
    }

    private fun sendMessage(text: String) {
        val currentUser = auth.currentUser ?: return
        
        val message = hashMapOf(
            "text" to text,
            "senderEmail" to (currentUser.email ?: ""),
            "senderName" to (currentUser.displayName ?: "Guest"),
            "timestamp" to System.currentTimeMillis(),
            "senderId" to currentUser.uid
        )

        firestore.collection("messages")
            .add(message)
            .addOnSuccessListener {
                binding.etMessage.text?.clear()
                binding.recyclerView.smoothScrollToPosition(messageAdapter.itemCount)
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.error_send_message), Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadMessages() {
        firestore.collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Toast.makeText(this, "Error loading messages", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                val messages = snapshots?.documents?.mapNotNull { doc ->
                    Message(
                        text = doc.getString("text") ?: "",
                        senderEmail = doc.getString("senderEmail") ?: "",
                        senderName = doc.getString("senderName") ?: "",
                        timestamp = doc.getLong("timestamp") ?: 0,
                        senderId = doc.getString("senderId") ?: ""
                    )
                } ?: emptyList()

                messageAdapter.setMessages(messages)
                if (messages.isNotEmpty()) {
                    binding.recyclerView.smoothScrollToPosition(messages.size - 1)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                auth.signOut()
                navigateToLogin()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
