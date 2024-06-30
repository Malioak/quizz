package com.example.quizz

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Resultado : AppCompatActivity() {
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

        val database = FirebaseDatabase.getInstance()
        val referenciaNotas = database.getReference("notas/resultado")

        referenciaNotas.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val matematica = snapshot.child("matematica").getValue(Int::class.java) ?: 0
                val portugues = snapshot.child("portugues").getValue(Int::class.java) ?: 0
                val historia = snapshot.child("historia").getValue(Int::class.java) ?: 0
                val geografia = snapshot.child("geografia").getValue(Int::class.java) ?: 0

                val total = matematica + portugues + historia + geografia
                val media = total / 4.0

                textoResultado.text = "Matemática: $matematica\nPortuguês: $portugues\nHistória: $historia\nGeografia: $geografia\nMédia: $media"

                textoMensagem.text = when {
                    media < 5 -> "Você não passou."
                    media < 7 -> "Você passou."
                    media < 9 -> "Parabéns, você passou."
                    else -> "Parabéns, você foi muito bem."
                }
            }

            override fun onCancelled(error: DatabaseError) {
                textoMensagem.text = "Erro ao carregar os dados."
            }
        })
    }
}