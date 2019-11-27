package es.iessaladillo.pedrojoya.pr04.ui.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import es.iessaladillo.pedrojoya.pr04.R
import es.iessaladillo.pedrojoya.pr04.base.observeEvent
import es.iessaladillo.pedrojoya.pr04.data.LocalRepository
import es.iessaladillo.pedrojoya.pr04.data.entity.Task
import es.iessaladillo.pedrojoya.pr04.data.iDs
import es.iessaladillo.pedrojoya.pr04.utils.hideKeyboard
import es.iessaladillo.pedrojoya.pr04.utils.invisibleUnless
import es.iessaladillo.pedrojoya.pr04.utils.setOnSwipeListener
import kotlinx.android.synthetic.main.tasks_activity.*
import kotlinx.android.synthetic.main.tasks_activity_item.*


class TasksActivity : AppCompatActivity() {

    private var mnuFilter: MenuItem? = null

    private val viewModel: TasksActivityViewModel by viewModels {

        TasksActivityViewModelFactory(LocalRepository, application)

    }

    private val listAdapter: TasksActivityAdapter = TasksActivityAdapter().apply {

        setOnItemClickListener(object: TasksActivityAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                updateItem(getItem(position))

            }


        })

    }

    private fun updateItem(task: Task) {

        viewModel.updateTaskCompletedState(task, !task.completed)

    }

    private fun shareTasks() {

        if (viewModel.lista!!.isNotEmpty()) {

            var tasks : MutableList<String> = mutableListOf()

            viewModel.lista!!.forEach{ tasks.add(String.format("%s: %s", it.concept, if (it.completed) "completado" else "no completado"))

                var text: String = ""

                tasks.forEach {text = text + it + "\n"}

                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type="text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Mis tareas")
                startActivity(Intent.createChooser(shareIntent, "Compartir") )

                }

        }

        else {

            Snackbar.make(lstTasks, getString(R.string.tasks_no_tasks_to_share), Snackbar.LENGTH_SHORT).show()

        }

    }

    private fun deleteTasks() {

        if (viewModel.lista!!.isNotEmpty()) {

            viewModel.deleteTasks()

        }

        else {

            Snackbar.make(lstTasks, getString(R.string.tasks_no_tasks_to_delete), Snackbar.LENGTH_SHORT).show()

        }

    }

    private fun markTaskssasComplete() {

        if (viewModel.lista!!.isNotEmpty()) {

            viewModel.markTasksAsCompleted()

        }

        else {

            Snackbar.make(lstTasks, getString(R.string.tasks_no_completed_tasks_yet), Snackbar.LENGTH_SHORT).show()

        }

    }

    private fun markTaskssasPending() {

        if (viewModel.lista!!.isNotEmpty()) {

            viewModel.markTasksAsPending()

        }

        else {

            Snackbar.make(lstTasks, getString(R.string.tasks_no_pending_tasks_yet), Snackbar.LENGTH_SHORT).show()

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.tasks_activity)

        setupViews()

        observeTasks()

    }

    fun addTask() {

        if (viewModel.isValidConcept(txtConcept.text.toString())) {

            viewModel.addTask(txtConcept.text.toString())

            txtConcept.setText("")

            imgAddTask.hideKeyboard()

        }

    }

    private fun setupViews() {

        imgAddTask.setOnClickListener {

            addTask()

        }

        txtConcept.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                addTask()
                true
            } else {
                false
            }
        }



        setupRecyclerView()

    }

    private fun observeTasks() {

        viewModel.tasks.observe(this) { showTasks(it) }

    }

    private fun setupRecyclerView() {

        lstTasks.run {

            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(context)

            itemAnimator = DefaultItemAnimator()

            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))

            adapter = listAdapter
            
            setOnSwipeListener { viewHolder, _ ->

                var task = listAdapter.getItem(viewHolder.adapterPosition)

                viewModel.deleteTask(task)

                Snackbar.make(lstTasks, getString(R.string.tasks_task_deleted, task.concept), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.tasks_recreate)) {

                        viewModel.insertTask(task) }
                    .show()

            }

        }

    }





    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity, menu)
        mnuFilter = menu.findItem(R.id.mnuFilter)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnuShare -> shareTasks()
            R.id.mnuDelete -> deleteTasks()
            R.id.mnuComplete -> markTaskssasComplete()
            R.id.mnuPending -> markTaskssasPending()
            R.id.mnuFilterAll -> viewModel.filterAll()
            R.id.mnuFilterPending -> viewModel.filterPending()
            R.id.mnuFilterCompleted -> viewModel.filterCompleted()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun checkMenuItem(@MenuRes menuItemId: Int) {
        lstTasks.post {
            val item = mnuFilter
            item?.let { menuItem ->
                menuItem.isChecked = true
            }
        }
    }

    private fun showTasks(tasks: List<Task>) {
        lstTasks.post {
            listAdapter.submitList(tasks)
            lblEmptyView.invisibleUnless(tasks.isEmpty())
        }
    }

}

