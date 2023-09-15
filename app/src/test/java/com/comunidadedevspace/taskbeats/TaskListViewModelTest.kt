package com.comunidadedevspace.taskbeats

import com.comunidadedevspace.taskbeats.data.local.TaskDao
import com.comunidadedevspace.taskbeats.presentation.TaskListViewModel
import org.mockito.kotlin.mock

class TaskListViewModelTest {

    private val taskDao: TaskDao = mock() //mock vem de mockito e cosigo fazer testes dubles

    //TaskListViewModel sp lista
    private val underTest: TaskListViewModel by lazy {
        TaskListViewModel(
            taskDao,//pede a dependencia e a dependencia dele eh o nosso dao

        )
    }

}

