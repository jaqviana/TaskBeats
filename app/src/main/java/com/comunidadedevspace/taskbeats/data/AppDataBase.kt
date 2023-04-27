package com.comunidadedevspace.taskbeats.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1) //entity q eh nossa task, q vai ser um array
abstract class AppDataBase: RoomDatabase() {
    abstract fun taskDao(): TaskDao //to expondo meu taskDao q vai ser meu acesso ao objeto/acesso a tabela/base de dados
}

