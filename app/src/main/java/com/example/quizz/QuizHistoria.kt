package com.example.quizz

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

class QuizHistoria : AppCompatActivity() {
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
        Pergunta("Qual foi o resultado da Revolução Francesa em relação à Mornaquia Absolutista?", "A monarquia constitucional foi estabelecida", listOf("Monarquia Absolutista Restaurada","O império foi proclamado.","A monarquia constitucional foi estabelecida","Uma república parlamentar foi instaurada.")) ,
        Pergunta("Qual batalha marcou o fim da Guerra dos Cem Anos entre a França e a Inglaterra?", "Batalha de Castillon", listOf(" Batalha de Hastings","Batalha de Agincourt","Batalha de Poitiers","Batalha de Castillon")),
        Pergunta("Quem foi o líder da Revolução Russa de 1917?", " Vladimir Lenin", listOf(" Vladimir Lenin","Joseph Stalin"," Leon Trotsky","Mikhail Gorbachev")),
        Pergunta("O que foi o Congresso de Viena, realizado em 1815??", "Uma conferência para redistribuir o poder na Europa após as Guerras Napoleônicas", listOf("Uma tentativa de Napoleão de conquistar a Europa","Uma conferência para redistribuir o poder na Europa após as Guerras Napoleônicas","Uma reunião para discutir a independência das colônias americanas","Um acordo para dividir a África entre as potências europeias")) ,
        Pergunta("Qual foi a principal causa da Guerra dos Trinta Anos (1618-1648)?", "Tensões religiosas e políticas na Europa Central", listOf("Disputas territoriais na Europa Oriental","Competição colonial entre as potências europeias","Conflitos entre monarquias absolutistas e parlamentares","Tensões religiosas e políticas na Europa Central")) ,
        Pergunta("Quem foi o líder do movimento de independência da Índia?:", "a) Mahatma Gandhi", listOf("Mahatma Gandhi","Jawaharlal Nehru"," Subhas Chandra Bose","Indira Gandhi")) ,
        Pergunta("O Tratado de Tordesilhas foi um acordo firmado entre:", "Portugal e Espanha", listOf(" Espanha e Itália","Portugal e Espanha"," Espanha e Holanda","Portugal e Itália")) ,
        Pergunta("Qual a data da independência do Brasil?", "7 de setembro de 1822?", listOf("7 de setembro de 1822","7 de setembro de 1832","7 de setembro de 1842","7 de setembro de 1812")) ,
        Pergunta("Qual foi o tratado que encerrou a Primeira Guerra Mundial?", "Tratado de Versalhes.", listOf("Tratado de Brest-Litovski","Tratado de Tordesilhas.","Tratado de Versalhes","Tratado de Paris.")) ,
        Pergunta("Qual evento marcou o início da Idade Moderna na Europa?", " A queda de Constantinopla", listOf(" A Reforma Protestante"," A queda de Constantinopla","O Renascimento","A descoberta da América")) ,
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_historia)
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
            "correto"
        }else{
            "Incorreto"
        }
        Toast.makeText(this,mensagem, Toast.LENGTH_SHORT).show()

        indicePerguntaAtual = (indicePerguntaAtual + 1) % perguntas.size

        if(indicePerguntaAtual == 0) {
            salvarResultadoNoFirebase()
            val intent = Intent(applicationContext, MenuActivity::class.java)
            intent.putExtra("respostasCorretasHistoria", contadorRespostasCorretas)
            startActivity(intent)
        } else {
            atualizarPergunta()
        }
        atualizarPergunta()
    }
    private fun salvarResultadoNoFirebase() {
        val userId = auth.currentUser?.uid ?: return
        val quizResultRef = database.reference.child("resultado").child(userId).child("historia")
        quizResultRef.setValue(contadorRespostasCorretas)
            .addOnSuccessListener {
                Toast.makeText(this, "Resultado salvo no Firebase", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao salvar resultado no Firebase", Toast.LENGTH_SHORT).show()
            }
    }
}