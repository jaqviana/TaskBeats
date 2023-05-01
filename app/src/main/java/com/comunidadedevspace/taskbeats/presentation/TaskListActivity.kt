package com.comunidadedevspace.taskbeats.presentation


import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable

class TaskListActivity : AppCompatActivity() {



    private lateinit var ctnContent: LinearLayout

    //Adapter
    private val adapter: TaskListAdapter by lazy {
        TaskListAdapter(::onListItemClicked )
    }

    private val viewModel: TaskListViewModel by lazy {
        TaskListViewModel.create(application)
    }

     private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val taskAction = data?.getSerializableExtra(TASK_ACTION_RESULT) as TaskAction

            viewModel.execute(taskAction)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        setSupportActionBar(findViewById(R.id.toolbar)) //colocar o toolbar q criamos


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

    //icializando o codigo
    override fun onStart() {
        super.onStart()
        listFromDataBase()
    }

    private fun deleteAll() {
        val taskAction = TaskAction( null, ActionType.DELETE_ALL.name)
       viewModel.execute(taskAction)
    }


    private fun listFromDataBase() {
           //Observer
            val listObserver = Observer<List<Task>>{ listTasks ->
                if(listTasks.isEmpty()) {
                    ctnContent.visibility = View.VISIBLE
                } else {
                    ctnContent.visibility = View.GONE
                }
                adapter.submitList(listTasks)
            }

            //Preciso linkar com o Live data
            viewModel.taskListLiveData.observe(this@TaskListActivity, listObserver)
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
  DELETE_ALL,
  UPDATE,
  CREATE,
}

//enum acima nao serializar ele, passar de uma tela pra outra por isso q passo por uma string e comparo name
data class TaskAction(
    val task: Task?,
    val actionType: String
) : Serializable

const val TASK_ACTION_RESULT = "TASK_ACTION_RESULT"

