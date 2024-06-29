package com.example.quizz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto.MenuActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val emailEditText = findViewById<EditText>(R.id.editTextText)
        val senhaEditText = findViewById<EditText>(R.id.editTextText2)
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            val email = emailEditText.text.toString()
            val senha = senhaEditText.text.toString()
            if (email.isNotEmpty() && senha.isNotEmpty()) {
                loginUsuario(email, senha)
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUsuario(email: String, senha: String) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login successful
                    val intent = Intent(this, MenuActivity::class.java)
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        intent.putExtra("userId", userId)
                    }
                    startActivity(intent)
                    Toast.makeText(this, "Login com Sucesso", Toast.LENGTH_SHORT).show()
                } else {
                    // Login failed
                    Toast.makeText(this, "Login Falhou: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
