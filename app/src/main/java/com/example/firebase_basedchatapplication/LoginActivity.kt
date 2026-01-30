package com.example.firebase_basedchatapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase_basedchatapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Check if user is already logged in
        if (auth.currentUser != null) {
            navigateToChat()
            return
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validateInput(email, password)) {
                loginUser(email, password)
            }
        }

        binding.tvRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validateInput(email, password)) {
                registerUser(email, password)
            }
        }

        binding.btnAnonymous.setOnClickListener {
            loginAnonymously()
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, getString(R.string.error_email), Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(this, getString(R.string.error_password), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun loginUser(email: String, password: String) {
        binding.btnLogin.isEnabled = false
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.btnLogin.isEnabled = true
                if (task.isSuccessful) {
                    navigateToChat()
                } else {
                    Toast.makeText(this, getString(R.string.error_login), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun registerUser(email: String, password: String) {
        binding.tvRegister.isEnabled = false
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.tvRegister.isEnabled = true
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    navigateToChat()
                } else {
                    Toast.makeText(this, getString(R.string.error_register), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginAnonymously() {
        binding.btnAnonymous.isEnabled = false
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                binding.btnAnonymous.isEnabled = true
                if (task.isSuccessful) {
                    navigateToChat()
                } else {
                    Toast.makeText(this, "Anonymous login failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToChat() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
