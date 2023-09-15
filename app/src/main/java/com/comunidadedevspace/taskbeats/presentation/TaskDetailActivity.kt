package com.comunidadedevspace.taskbeats.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.local.Task
import com.google.android.material.snackbar.Snackbar

class TaskDetailActivity : AppCompatActivity() {

   private var task: Task? = null
   private lateinit var btnDone: Button


    //criando (a instacncia) do viewmodel                                                                    //factor vai falar como criar meu viewmodel
   private val viewModel: TaskDetailViewModel by viewModels {
        TaskDetailViewModel.getVMFactory(application)} // by viewModels nao consome memoria (val e espaco na memoria) so qdo usar
    companion object {
        private const val TASK_DETAIL_EXTRA = "task.extra.detail"

        fun start(context: Context, task: Task?): Intent {
            val intent = Intent(context, TaskDetailActivity::class.java)
                .apply {
                    putExtra(TASK_DETAIL_EXTRA, task)
                }
            return intent
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        //pq criei meu proprio toolbar
        setSupportActionBar(findViewById(R.id.toolbar))

        //Recuperar string da tela anterior
        task = intent.getSerializableExtra(TASK_DETAIL_EXTRA) as Task?

        val edtTitle = findViewById<EditText>(R.id.edt_task_title)
        val edtDescription = findViewById<EditText>(R.id.edt_task_description)
        btnDone = findViewById<Button>(R.id.btn_done)

        if(task != null){
            edtTitle.setText(task!!.title)
            edtDescription.setText(task!!.description)
        }

        //Para concluir a acao
        btnDone.setOnClickListener{
        //Para recuperar o title qdo clico no  botao
            val title = edtTitle.text.toString()
            val desc = edtDescription.text.toString()

            if(title.isNotEmpty() && desc.isNotEmpty()){
                if(task == null){
                    addOrUpdateTask(0, title, desc, ActionType.CREATE)
                }else{
                    addOrUpdateTask(task!!.id, title, desc, ActionType.UPDATE)
                }

            }else{
                showMassage(it, "Fields are required")
            }
        }



        //setar um novo texto na tela
       // tvTitle.text = task?.title?: "Add new task"
    }
    private fun addOrUpdateTask(
        id: Int,
        title: String,
        description: String,
        actionType: ActionType
    ){
        val task = Task(id, title, description)
        performAction(task, actionType)
    }
    //inflar/colocar o menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_task_detail, menu)
        return true
    }
    //acao do menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task -> {

                if (task != null) {
                   performAction(task!!, ActionType.DELETE)

                } else {
                    showMassage(btnDone, "Item not found")
                }

                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    private fun performAction(task: Task, actionType: ActionType){
            val taskAction = TaskAction(task, actionType.name)
            viewModel.execute(taskAction) //para chamar a funcao executar eu preciso ter um viewmodel, para ter um viewmodel eu preciso criar uma instancia desse viewmodel (linha 40 -  private val viewModel: TaskDetailViewModel by viewModels {factory})
             finish()
    }

    private fun showMassage(view: View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

}


