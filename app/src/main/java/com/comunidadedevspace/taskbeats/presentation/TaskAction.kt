package com.comunidadedevspace.taskbeats.presentation

import com.comunidadedevspace.taskbeats.data.Task
import java.io.Serializable

//CRUD (Create, Read, Update, Delete)
enum class ActionType {
    DELETE,
    UPDATE,
    CREATE
}

//enum acima nao serializar ele, passar de uma tela pra outra por isso q passo por uma string e comparo name
data class TaskAction(
    val task: Task?,
    val actionType: String
) : Serializable