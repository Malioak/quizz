package com.example.quizz

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.Touch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Resultado : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var textoResultado: TextView
    private lateinit var textoMensagem: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textoResultado = findViewById(R.id.textoResultado)
        textoMensagem = findViewById(R.id.textoMensagem)
        val userId = Firebase.auth.currentUser?.uid.toString()
        val database = FirebaseDatabase.getInstance()
        val referenciaNotas = database.getReference("resultado").child(userId)

        referenciaNotas.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                val matematica = snapshot.child("matematica").getValue(Int::class.java) ?: 0
                val portugues = snapshot.child("portugues").getValue(Int::class.java) ?: 0
                val historia = snapshot.child("historia").getValue(Int::class.java) ?: 0
                val geografia = snapshot.child("geografia").getValue(Int::class.java) ?: 0

                val total = matematica + portugues + historia + geografia
                val media = total / 4.0

                textoResultado.text = "Historia: $historia\nGeografia: $geografia\nPortugues: $portugues\nMatematica: $matematica"

                textoMensagem.text = when {
                    media < 5 -> "Sua média é $media, você não passou."
                    media < 7 -> "Sua média é $media, você passou."
                    media < 9 -> "Sua média é $media, parabéns, você passou."
                    else -> "Parabéns sua média é $media, você foi muito bem."
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onCancelled(error: DatabaseError) {
                textoMensagem.text = "Erro ao carregar os dados."
            }
        })
    }
}
