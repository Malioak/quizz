package com.example.quizz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

private var mat = 1
private var geo = 1
private var his = 1
private var por = 1

class MenuActivity : AppCompatActivity() {
    private lateinit var botaoMatematica: ImageButton
    private lateinit var botaoHistoria: ImageButton
    private lateinit var botaoGeografia: ImageButton
    private lateinit var botaoPortugues: ImageButton
    private lateinit var button3: Button

    private var botaoMatematicaClicado = false
    private var botaoHistoriaClicado = false
    private var botaoGeografiaClicado = false
    private var botaoPortuguesClicado = false
    private var totalBotoesClicados = 0

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        // Verifica se o usuário está autenticado
        if (auth.currentUser == null) {
            Log.e("MenuActivity", "Usuário não autenticado, redirecionando para a tela de login")
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }


        botaoMatematica = findViewById(R.id.botaoMatematica)
        botaoGeografia = findViewById(R.id.botaoGeografia)
        botaoHistoria = findViewById(R.id.botaoHistoria)
        botaoPortugues = findViewById(R.id.botaoPortugues)
        button3 = findViewById(R.id.button3)  // Adicione essa linha

        botaoMatematica.setOnClickListener {
            if (!botaoMatematicaClicado) {
                botaoMatematicaClicado = true
                intentM()

            }
        }

        botaoHistoria.setOnClickListener {
            if (!botaoHistoriaClicado) {
                botaoHistoriaClicado = true
                intentH()
            }
        }

        botaoGeografia.setOnClickListener {
            if (!botaoGeografiaClicado) {
                botaoGeografiaClicado = true
                intentG()

            }
        }

        botaoPortugues.setOnClickListener {
            if (!botaoPortuguesClicado) {
                botaoPortuguesClicado = true
                intentP()
            }
        }

        button3.setOnClickListener {
            intentR()
        }
    }



    private fun intentM() {
        totalBotoesClicados++

        if (mat == 1) {
            val intent = Intent(applicationContext, QuizMatematica::class.java)
            startActivity(intent)
            mat = 0
        }
    }

    private fun intentG() {
        if (geo == 1) {
            val intent = Intent(applicationContext, QuizGeografia::class.java)
            startActivity(intent)
            geo = 0
        }
    }

    private fun intentH() {
        if (his == 1) {
            val intent = Intent(applicationContext, QuizHistoria::class.java)
            startActivity(intent)
            his = 0
        }
    }

    private fun intentP() {
        if (por == 1) {
            val intent = Intent(applicationContext, QuizPortugues::class.java)
            startActivity(intent)
            por = 0
        }
    }

    private fun intentR() {
        val intent = Intent(applicationContext, Resultado::class.java)
        startActivity(intent)
    }
}
