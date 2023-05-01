package com.comunidadedevspace.taskbeats.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao //vai acessar a base de dados
interface TaskDao   {

    //essa funcao vai receber uma classe que eh uma entity e por enquanto nao vai me devolver nada so vai inserir e nao vai fazer mais nada
    @Insert(onConflict = OnConflictStrategy.REPLACE) //qdo to tentando iserir uma tarefa mas tem tarefa igual entao faz o replace
    fun insert(task: Task)

    @Query("Select * from task")
    fun getAll(): LiveData<List<Task>>

        @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(task: Task)

    //Delete all
    @Query("Delete from task")
    fun deleteALl()

    //Delete pelo id
    @Query("Delete from task WHERE id =:id")
    fun deleteById(id: Int)
}