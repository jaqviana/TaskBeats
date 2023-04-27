package com.comunidadedevspace.taskbeats


import android.app.Activity
import android.icu.text.CaseMap.Title
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.io.Serializable

class TaskListActivity : AppCompatActivity() {



    private lateinit var ctnContent: LinearLayout

    //Adapter
    private val adapter: TaskListAdapter by lazy {
        TaskListAdapter(::onListItemClicked )
    }

    private val dataBase by lazy { //by lazy esse codigo so vai executar qdo eu precisar utilizar o database
        Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "taskbeats-database" //eh o nome da nossa base de dados
        ).build()

    }
    private val dao by lazy { //esse codigo so vai ser utilizado qdo for utilizar o dao
        dataBase.taskDao()
    }

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {

            val data = result.data
            val taskAction = data?.getSerializableExtra(TASK_ACTION_RESULT) as TaskAction
            val task: Task = taskAction.task

            when (taskAction.actionType) {
                ActionType.DELETE.name -> deleteByID(task.id)
                ActionType.CREATE.name -> insertIntoDataBase(task)
                ActionType.UPDATE.name -> updateIntoDataBase(task)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        setSupportActionBar(findViewById(R.id.toolbar)) //colocar o toolbar q criamos

        listFromDataBase()
        ctnContent = findViewById(R.id.ctn_content)


        //Recyclerview
        val rvTasks: RecyclerView = findViewById(R.id.rv_task_list)
        rvTasks.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            openTaskListDetail( null)

        }

    }

    //CRUDE eh feio de:
    //CREATE = que eh o insert
    //READ que ah a listagem q estamos fazendo "listFromDataBase()"
    //Update
    //Delete


    private fun insertIntoDataBase(task: Task) {
        CoroutineScope(IO).launch {
            dao.insert(task) //insiro a nova tarefa
            listFromDataBase() //chamo a list de tarefas
        }

    }

    private fun updateIntoDataBase(task: Task) {
        CoroutineScope(IO).launch {
            dao.update(task) //update da tarefa
            listFromDataBase() //chamo a list de tarefas
        }

    }

    private fun deleteAll(){
        CoroutineScope(IO).launch {
            dao.deleteALl()
            listFromDataBase() //deleta no banco de dados e atuliza nossa lista do recyclereview
        }
    }

    private fun deleteByID(id: Int){
        CoroutineScope(IO).launch {
            dao.deleteById(id)
            listFromDataBase() //deleta no banco de dados e atuliza nossa lista do recyclereview
        }
    }
    private fun listFromDataBase(){
        CoroutineScope(IO).launch {
            val myDataBaseList: List<Task> = dao.getAll()
            adapter.submitList(myDataBaseList)

        }
    }

    private fun showMassage(view: View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }
    private fun onListItemClicked (task: Task){
        openTaskListDetail(task)
    }

    private fun openTaskListDetail(task: Task? = null){
        val intent = TaskDetailActivity.start(this, task)
        startForResult.launch(intent)

    }
    //inflar/colocar o menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_task_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_task -> {
                 deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

}

//CRUD (Create, Read, Update, Delete)
enum class ActionType {
  DELETE,
  UPDATE,
  CREATE,
}

//enum acima nao serializar ele, passar de uma tela pra outra por isso q passo por uma string e comparo name
data class TaskAction(
    val task: Task,
    val actionType: String
) : Serializable

const val TASK_ACTION_RESULT = "TASK_ACTION_RESULT"

