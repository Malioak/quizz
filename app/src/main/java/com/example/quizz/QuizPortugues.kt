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

class QuizPortugues : AppCompatActivity() {
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
        Pergunta("Qual é o conceito de metáfora na linguística?", "Figura de linguagem que consiste na transferência de significado de uma palavra ou expressão para outra.", listOf("Troca de uma palavra por outra de sentido oposto.",
            "Utilização de palavras de sons semelhantes para criar efeito sonoro.",
            "Figura de linguagem que consiste na transferência de significado de uma palavra ou expressão para outra.",
            "Repetição de uma mesma palavra ou expressão no início de versos ou frases.")),
        Pergunta("O que é uma oração subordinada?", "Oração que depende sintaticamente de outra oração, denominada oração principal.", listOf("Oração formada por um único verbo.",
            "Oração que expressa uma ordem ou pedido.",
            "Oração que depende sintaticamente de outra oração, denominada oração principal.",
            "Oração que expressa uma ação habitual.")),
        Pergunta("O que é uma antítese na produção textual?", "Figura de linguagem que estabelece uma contraposição entre dois termos ou ideias.", listOf("Inversão da ordem habitual das palavras em uma frase.",
            "Repetição de sons consonantais no início de palavras próximas.",
            "Figura de linguagem que estabelece uma contraposição entre dois termos ou ideias.",
            "Utilização de palavras de sentido oposto.")),
        Pergunta("O que é uma crase na língua portuguesa?", "Fusão da preposição 'a' com o artigo feminino 'a' ou com o pronome demonstrativo 'a'", listOf("Inclusão de um sinal gráfico no final de uma frase interrogativa.",
            "Repetição de um mesmo som ou grupo de sons.",
            "Fusão da preposição 'a' com o artigo feminino 'a' ou com o pronome demonstrativo 'a'",
            "Inserção de uma vogal entre duas consoantes para facilitar a pronúncia.")),
        Pergunta("Qual é a função do conectivo 'portanto' em um texto?", "Indicar uma conclusão ou consequência a partir do que foi exposto anteriormente.", listOf("Estabelecer uma comparação entre dois elementos.",
            "Apresentar uma condição necessária para que algo ocorra.",
            "Indicar uma conclusão ou consequência a partir do que foi exposto anteriormente.",
            "Expressar uma hipótese ou possibilidade.")),
        Pergunta("O que é uma metonímia na linguagem?", "Figura de linguagem que consiste na substituição de um termo por outro, havendo uma relação de proximidade ou associação entre eles.", listOf("Substituição de um termo por outro de sentido oposto.",
            "Repetição de uma mesma estrutura sintática ao longo de um texto.",
            "Figura de linguagem que consiste na substituição de um termo por outro, havendo uma relação de proximidade ou associação entre eles.",
            "Utilização de palavras de sons semelhantes para criar efeito sonoro.")),
        Pergunta("O que é uma ambiguidade na produção textual?", "Presença de uma expressão que pode ser interpretada de mais de uma maneira.", listOf("Repetição desnecessária de um termo ao longo do texto.",
            "Incoerência entre as ideias apresentadas em um texto.",
            "Presença de uma expressão que pode ser interpretada de mais de uma maneira.",
            "Ausência de pontuação adequada para marcar as pausas e entonações.")),
        Pergunta("Qual é o conceito de polissemia na semântica?", "Característica de uma palavra ter múltiplos significados.", listOf("Capacidade de uma palavra transmitir uma ideia de forma clara e precisa.",
            "Conjunto de palavras que possuem apenas um significado.",
            "Característica de uma palavra ter múltiplos significados.",
            "Incoerência entre as ideias apresentadas em um texto.")),
        Pergunta("O que é uma elipse na gramática?", "Omissão de termos já mencionados e facilmente identificáveis pelo contexto.", listOf("Repetição desnecessária de uma palavra ou expressão.",
            "Inserção de termos redundantes para enfatizar uma ideia.",
            "Omissão de termos já mencionados e facilmente identificáveis pelo contexto.",
            "Repetição de uma mesma estrutura sintática ao longo de um texto.")),
        Pergunta("Qual é o papel do adjunto adverbial em uma oração?", "Modificar o sentido do verbo, do adjetivo ou de outro advérbio, indicando circunstância.", listOf("Indicar a pessoa, o lugar, o tempo, a maneira, a quantidade ou a intensidade do verbo.",
            "Estabelecer uma relação de causa e consequência entre duas orações.",
            "Modificar o sentido do verbo, do adjetivo ou de outro advérbio, indicando circunstância.",
            "Apresentar uma condição necessária para que algo ocorra.")),
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_portugues)
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
            intent.putExtra("respostasCorretasPortugues", contadorRespostasCorretas)
            startActivity(intent)
        } else {
            atualizarPergunta()
        }
        atualizarPergunta()
    }
    private fun salvarResultadoNoFirebase() {
        val userId = auth.currentUser?.uid ?: return
        val quizResultRef = database.reference.child("resultado").child(userId).child("portugues")
        quizResultRef.setValue(contadorRespostasCorretas)
            .addOnSuccessListener {
                Toast.makeText(this, "Resultado salvo no Firebase", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao salvar resultado no Firebase", Toast.LENGTH_SHORT).show()
            }
    }
}