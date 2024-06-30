package com.example.quizz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class QuizGeografia: AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var textoPergunta1: TextView
    private lateinit var botaoResposta1: Button
    private lateinit var botaoResposta2: Button
    private lateinit var botaoResposta3: Button
    private lateinit var botaoResposta4: Button

    private var indicePerguntaAtual = 0
    private var contadorRespostasCorretas = 0

    private val perguntas = listOf(
        Pergunta("Qual é o maior rio do mundo em volume de água?", "Rio Amazonas", listOf("Rio Nilo", "Rio Amazonas", "Rio Yangtzé", "Rio Mississipi")),
        Pergunta("Qual é o ponto mais alto da Terra?", "Monte Everest", listOf("Monte Kilimanjaro", "Monte K2", "Monte Everest", "Monte Fuji")),
        Pergunta("Qual é o maior deserto do mundo?", "Deserto do Saara", listOf("Deserto de Gobi", "Deserto do Saara", "Deserto do Atacama", "Deserto da Arábia")),
        Pergunta("Qual é o maior oceano do mundo?", "Oceano Pacífico", listOf("Oceano Atlântico", "Oceano Índico", "Oceano Ártico", "Oceano Pacífico")),
        Pergunta("Qual é o país mais populoso do mundo?", "india", listOf("Índia", "Estados Unidos", "Brasil", "China")),
        Pergunta("Qual é o menor país do mundo em área territorial?", "Vaticano", listOf("Mônaco", "Nauru", "Vaticano", "Maldivas")),
        Pergunta("Qual é o maior país em área territorial?", "Rússia", listOf("Canadá", "Estados Unidos", "China", "Rússia")),
        Pergunta("Qual é o nome da capital do Canadá?", "Ottawa", listOf("Toronto", "Montreal", "Vancouver", "Ottawa")),
        Pergunta("Qual é o nome do deserto localizado na América do Norte?", "Deserto do Mojave", listOf("Deserto de Sonora", "Deserto do Atacama", "Deserto do Mojave", "Deserto do Chihuahua")),
        Pergunta("Qual é o ponto mais baixo da Terra?", "Fossa das Marianas", listOf("Grande Fossa Oceânica", "Fossa das Marianas", "Fossa da Groenlândia", "Fossa de Tonga")),
    )


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_geografia)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        database = Firebase.database
        auth = Firebase.auth

        textoPergunta1 = findViewById(R.id.textoPergunta1)
        botaoResposta1 = findViewById(R.id.botaoResposta1)
        botaoResposta2 = findViewById(R.id.botaoResposta2)
        botaoResposta3 = findViewById(R.id.botaoResposta3)
        botaoResposta4 = findViewById(R.id.botaoResposta4)
        atualizarPergunta()
        botaoResposta1.setOnClickListener { verificarRespostas(perguntas[indicePerguntaAtual].resposta[0]) }
        botaoResposta2.setOnClickListener { verificarRespostas(perguntas[indicePerguntaAtual].resposta[1]) }
        botaoResposta3.setOnClickListener { verificarRespostas(perguntas[indicePerguntaAtual].resposta[2]) }
        botaoResposta4.setOnClickListener { verificarRespostas(perguntas[indicePerguntaAtual].resposta[3]) }
    }

    private fun atualizarPergunta(){
        val perguntaAtual = perguntas[indicePerguntaAtual]
        textoPergunta1.text = perguntaAtual.texto
        botaoResposta1.text = perguntaAtual.resposta[0]
        botaoResposta2.text = perguntaAtual.resposta[1]
        botaoResposta3.text = perguntaAtual.resposta[2]
        botaoResposta4.text = perguntaAtual.resposta[3]
    }


    private fun verificarRespostas(resposta: String){
        val mensagem = if(resposta == perguntas[indicePerguntaAtual].respostaCorreta){
            contadorRespostasCorretas++
            "Correto"
        }else{
            "Incorreto"
        }
        Toast.makeText(this,mensagem, Toast.LENGTH_SHORT).show()

        indicePerguntaAtual = (indicePerguntaAtual + 1) % perguntas.size

        if(indicePerguntaAtual == 0) {
            salvarResultadoNoFirebase()
            val intent = Intent(applicationContext, MenuActivity::class.java)
            intent.putExtra("respostasCorretasGeografia", contadorRespostasCorretas)
            startActivity(intent)
        } else {
            atualizarPergunta()
        }
        atualizarPergunta()
    }
    private fun salvarResultadoNoFirebase() {
        val userId = auth.currentUser?.uid ?: return
        val quizResultRef = database.reference.child("resultado").child(userId).child("geografia")
        quizResultRef.setValue(contadorRespostasCorretas)
            .addOnSuccessListener {
                Toast.makeText(this, "Resultado salvo no Firebase", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao salvar resultado no Firebase", Toast.LENGTH_SHORT).show()
            }
    }
}