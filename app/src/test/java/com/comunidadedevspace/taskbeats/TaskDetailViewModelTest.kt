package com.comunidadedevspace.taskbeats

import com.comunidadedevspace.taskbeats.data.local.Task
import com.comunidadedevspace.taskbeats.data.local.TaskDao
import com.comunidadedevspace.taskbeats.presentation.ActionType
import com.comunidadedevspace.taskbeats.presentation.TaskAction
import com.comunidadedevspace.taskbeats.presentation.TaskDetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class TaskDetailViewModelTest {

    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val taskDao: TaskDao = mock() //mock vem de mockito e cosigo fazer testes dubles


    private val underTest: TaskDetailViewModel by lazy {
        TaskDetailViewModel(
            taskDao,//pede a dependencia e a dependencia dele eh o nosso dao

        )
    }
    @Test
    fun update_task() = runTest {
        //Given
        val task = Task( //UPDATE PRECISO PASSAR UMA TASK
            id = 1,
            title = "title",
            description = "description"
        )
        val taskAction = TaskAction(
            task = task,
            actionType = ActionType.UPDATE.name
        )
        //When
        underTest.execute(taskAction) //o q vai executar

        //Then
        verify(taskDao).update(task)
    }

    //outro caso de uso
    @Test
    fun delete_task() = runTest {
        //Given
        val task = Task( //UPDATE PRECISO PASSAR UMA TASK
            id = 1,
            title = "title",
            description = "description"
        )
        val taskAction = TaskAction(
            task = task,
            actionType = ActionType.DELETE.name
        )
        //When
        underTest.execute(taskAction) //o q vai executar

        //Then
        verify(taskDao).deleteById(task.id) //delete ById tem q pegar o mesmo ID

    }
    @Test
    fun create_task() = runTest {
        //Given
        val task = Task( //UPDATE PRECISO PASSAR UMA TASK
            id = 1,
            title = "title",
            description = "description"
        )
        val taskAction = TaskAction(
            task = task,
            actionType = ActionType.CREATE.name
        )
        //When
        underTest.execute(taskAction) //o q vai executar

        //Then
        verify(taskDao).insert(task)
    }
}