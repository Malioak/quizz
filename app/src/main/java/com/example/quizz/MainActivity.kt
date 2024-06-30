package com.example.quizz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialização do FirebaseAuth
        auth = Firebase.auth

        val emailEditText = findViewById<EditText>(R.id.editTextText)
        val senhaEditText = findViewById<EditText>(R.id.editTextText2)
        val button = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)

        button.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val senha = senhaEditText.text.toString().trim()
            if (email.isNotEmpty() && senha.isNotEmpty()) {
                loginUsuario(email, senha)
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        button2.setOnClickListener{
            val email = emailEditText.text.toString().trim()
            val senha = senhaEditText.text.toString().trim()
            auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this){task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Foi", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Falhou", Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }

    private fun loginUsuario(email: String, senha: String) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Login", "Login bem-sucedido")

                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Login com Sucesso", Toast.LENGTH_SHORT).show()

                } else {
                    // Adicionando um log para depuração
                    Log.e("Login", "Login falhou", task.exception)
                    Toast.makeText(this, "Login Falhou: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Login", "Erro de login", exception)
                Toast.makeText(this, "Erro de login: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
