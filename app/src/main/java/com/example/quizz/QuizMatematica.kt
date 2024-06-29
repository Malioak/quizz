package com.example.projeto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizz.Pergunta
import com.example.quizz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class QuizMatematica : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var textoPergunta1: TextView
    private lateinit var botaoResposta1: Button
    private lateinit var botaoResposta2: Button
    private lateinit var botaoResposta3: Button
    private lateinit var botaoResposta4: Button

    private var indicePerguntaAtual = 0
    private var contadorRespostasCorretas = 0

    private val perguntas= listOf(
        Pergunta("Qual é o valor do número pi (π)?", "3.14159", listOf("3.14159", "2.71828", "1.61803", "4.66920")),
        Pergunta("Quanto é a raiz quadrada de -1?", "i (unidade imaginária)", listOf("1", "-1", "i (unidade imaginária)", "0")),
        Pergunta("Qual é o resultado da integral de e elevado a x, em relação a x?", "e elevado a x", listOf("x", "e", "1/x", "e elevado a x")),
        Pergunta("Qual é a derivada de seno(x)?", "cos(x)", listOf("sen(x)", "cos(x)", "-sen(x)", "-cos(x)")),
        Pergunta("Qual é o resultado de 0! (fatorial de 0)?", "1", listOf("0", "1", "2", "π")),
        Pergunta("Qual é o número de Euler, e, aproximadamente?", "2.71828", listOf("3.14159", "1.41421", "2.71828", "0.57722")),
        Pergunta("Qual é a fórmula para calcular a soma dos termos de uma progressão geométrica finita?", "a * (1 - r^n) / (1 - r)", listOf("a * (1 - r^n)", "a * (r^n - 1)", "a * (1 + r^n)", "a * (1 - r^n) / (1 - r)")),
        Pergunta("Qual é o resultado de ln(e)?", "1", listOf("0", "1", "e", "π")),
        Pergunta("Qual é o valor do cosseno de 0 graus?", "1", listOf("0", "1", "-1", "π/2")),
        Pergunta("Qual é o valor de seno(pi/2)?", "1", listOf("0", "1", "-1", "π/2")),
    )

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_matematica)
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
        botaoResposta4.setOnClickListener { verificarRespostas(perguntas[indicePerguntaAtual].resposta[3]) }}

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
            intent.putExtra("respostasCorretasMatematica", contadorRespostasCorretas)
            startActivity(intent)
        } else {
            atualizarPergunta()
        }
    }
    private fun salvarResultadoNoFirebase() {
        val userId = auth.currentUser?.uid ?: return
        val quizResultRef = database.reference.child("matematica").child(userId).child("resultado")
        quizResultRef.setValue(contadorRespostasCorretas)
            .addOnSuccessListener {
                Toast.makeText(this, "Resultado salvo no Firebase", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao salvar resultado no Firebase", Toast.LENGTH_SHORT).show()
            }
    }

}