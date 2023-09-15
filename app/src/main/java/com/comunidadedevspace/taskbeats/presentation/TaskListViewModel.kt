package com.comunidadedevspace.taskbeats.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.comunidadedevspace.taskbeats.TaskBeatsApplication
import com.comunidadedevspace.taskbeats.data.local.Task
import com.comunidadedevspace.taskbeats.data.local.TaskDao

//classe q preciso ter dependencia que eh a classe Dao
class TaskListViewModel(taskDao: TaskDao): ViewModel() { //TaskListViewModel so lista

    //assim tenho meu livedata no viewmodel
    val taskListLiveData: LiveData<List<Task>> = taskDao.getAll()


    companion object{

        //essa fun vai me retornar/criar um viewmodel
        fun create(application: Application):TaskListViewModel {
            //para e pegar o database
            val dataBaseInstance = (application as TaskBeatsApplication).getAppDataBase()
            val dao = dataBaseInstance.taskDao()
           //agora que tenho um task Dao vou conseguir criar um viewModel, para criar um viewModel preciso passar um dao como parametro de contrucao (dao)
            return TaskListViewModel(dao)
        }
    }

}