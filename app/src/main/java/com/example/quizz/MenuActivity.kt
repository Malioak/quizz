package com.example.projeto

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizz.R

private var mat = 1
private var geo = 1
private var his = 1
private var por = 1



class MenuActivity : AppCompatActivity() {
    private lateinit var botaoMaatematica: ImageButton
    private lateinit var botaoHistoria: ImageButton
    private lateinit var botaoGeografia: ImageButton
    private lateinit var botaoPortugues: ImageButton

    private var botaoMatematicaClicado = false
    private var botaoHistoriaClicado = false
    private var botaoGeografiaClicado = false
    private var botaoPortuguesClicado = false
    private var totalBotoesClicados = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = intent.getStringExtra("userId")



        botaoMaatematica = findViewById(R.id.botaoMatematica)
        botaoGeografia = findViewById(R.id.botaoGeografia)
        botaoHistoria = findViewById(R.id.botaoHistoria)
        botaoPortugues = findViewById(R.id.botaoPortugues)


        botaoMaatematica.setOnClickListener {
            if (!botaoMatematicaClicado) {
                botaoMatematicaClicado = true
                intentM()
                /*verificarTodosBotoesClicados()*/
            }
        }

        /*botaoHistoria.setOnClickListener {
            if (!botaoHistoriaClicado) {
                botaoHistoriaClicado = true
                intentG()
                verificarTodosBotoesClicados()
            }
        }*/

/*botaoGeografia.setOnClickListener {
    if (!botaoGeografiaClicado) {
        botaoGeografiaClicado = true

        intentH()
        verificarTodosBotoesClicados()
    }
}*/

/*botaoPortugues.setOnClickListener {
if (!botaoPortuguesClicado) {
botaoPortuguesClicado = true
intentP()
verificarTodosBotoesClicados()
}
}*/

}
/*private fun verificarTodosBotoesClicados() {
if (mat==0 && geo==0 && his==0 && por==0) {
val intent = Intent(applicationContext, Resultado::class.java)
startActivity(intent)
}
}*/



private fun intentM() {
totalBotoesClicados++

if(mat == 1) {
val intent = Intent(applicationContext, QuizMatematica::class.java)
startActivity(intent)

mat = 0
}

}

/*private fun intentG(){


if(geo == 1 ){
val intent = Intent(applicationContext, QuizGeografia::class.java)
startActivity(intent)

geo = 0
}
}

private fun intentH(){

if(his == 1 ){
val intent = Intent(applicationContext, QuizHistoria::class.java)
startActivity(intent)

his = 0
}
}
private fun intentP(){
if(por == 1 ) {
val intent = Intent(applicationContext, QuizPortugues::class.java)
startActivity(intent)

por=0
} */
}

//}