package com.comunidadedevspace.taskbeats.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comunidadedevspace.taskbeats.TaskBeatsApplication
import com.comunidadedevspace.taskbeats.data.Task
import com.comunidadedevspace.taskbeats.data.TaskDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//classe q preciso ter dependencia que eh a classe Dao
class TaskListViewModel(
    private val taskDao: TaskDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
 ): ViewModel() {

    //assim tenho meu livedata no viewmodel
    val taskListLiveData: LiveData<List<Task>> = taskDao.getAll()

    fun execute(taskAction: TaskAction) {
        when (taskAction.actionType) {
            ActionType.DELETE.name -> deleteByID(taskAction.task!!.id)
            ActionType.CREATE.name -> insertIntoDataBase(taskAction.task!!)
            ActionType.UPDATE.name -> updateIntoDataBase(taskAction.task!!)
            ActionType.DELETE_ALL.name -> deleteAll()
        }
    }

    private fun deleteByID(id: Int){
        viewModelScope.launch(dispatcher) {
            taskDao.deleteById(id)
        }

    }

    private fun insertIntoDataBase(task: Task) {
        viewModelScope.launch(dispatcher) {
            taskDao.insert(task) //insiro a nova tarefa

        }

    }

    private fun updateIntoDataBase(task: Task) {
        viewModelScope.launch(dispatcher) {
            taskDao.update(task) //update da tarefa

        }

    }

    private fun deleteAll(){
       viewModelScope.launch(dispatcher) {
           taskDao.deleteALl()

        }
    }
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