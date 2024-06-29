package com.example.quizz

data class  Pergunta(
val texto: String,
val respostaCorreta: String,
val resposta: List<String>
)