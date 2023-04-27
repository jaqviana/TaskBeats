package com.comunidadedevspace.taskbeats

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Task(
    //Task com mesmo id uma delas sera descartada, manter cosistencia no banco de dados
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, //toda vez q acrescentar mais uma tarefa vai acrescentar +1 nesse numero vai gerar sozinho
    val title: String,
    val description: String
 ) : Serializable