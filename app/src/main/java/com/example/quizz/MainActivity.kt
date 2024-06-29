package com.example.quizz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inicializa o Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configura o listener para o botão de login
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val email = findViewById<EditText>(R.id.editTextText).text.toString()
            val senha = findViewById<EditText>(R.id.editTextText2).text.toString()
            loginUser(email, senha)
        }
    }

    private fun loginUser(email: String, senha: String) {
        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Login bem-sucedido
                val intent = Intent(this, Menu::class.java)
                intent.putExtra("resultado", 1) // Passa o resultado do login como 1 (sucesso)
                startActivity(intent)
                Toast.makeText(this, "Login com Sucesso", Toast.LENGTH_SHORT).show()
            } else {
                // Login falhou
                Toast.makeText(this, "Login Falhou: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}