package com.comunidadedevspace.taskbeats

import com.comunidadedevspace.taskbeats.data.Task
import com.comunidadedevspace.taskbeats.data.TaskDao
import com.comunidadedevspace.taskbeats.presentation.ActionType
import com.comunidadedevspace.taskbeats.presentation.TaskAction
import com.comunidadedevspace.taskbeats.presentation.TaskListViewModel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class TaskListViewModelTest {

    private val taskDao: TaskDao = mock() //mock vem de mockito e cosigo fazer testes dubles

    private val underTest: TaskListViewModel by lazy {
        TaskListViewModel(
            taskDao,//pede a dependencia e a dependencia dele eh o nosso dao
            UnconfinedTestDispatcher() //esse aqui cria o fake, nao preciso criar
        )
    }

    //Testes cases Delete_all

    @Test //este @test vem da biblioteca junit pra executar meus testes
    fun delete_all() =
        runTest {//para testar qdo tem coroutine (usado para mudar de thread) preciso escrever runTest
            //Given
            val taskAction = TaskAction(
                task = null, //para deleter tdo nao precisa passar uma task pode ser nulo, mas preciso passar uma actionType
                actionType = ActionType.DELETE_ALL.name
            )
            //When
            underTest.execute(taskAction) //o q vai executar

            //Then
            verify(taskDao).deleteALl() //verify vem do mockito, para verificar q ele fez o delete all
        }

    //outro caso de uso
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

