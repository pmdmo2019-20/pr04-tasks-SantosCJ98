package es.iessaladillo.pedrojoya.pr04.ui.main

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import es.iessaladillo.pedrojoya.pr04.R
import es.iessaladillo.pedrojoya.pr04.base.Event
import es.iessaladillo.pedrojoya.pr04.data.Repository
import es.iessaladillo.pedrojoya.pr04.data.entity.Task
import kotlinx.android.synthetic.main.tasks_activity.*
import kotlinx.android.synthetic.main.tasks_activity_item.*

class TasksActivityViewModel(private val repository: Repository,
                             private val application: Application) : ViewModel() {

    // Estado de la interfaz

    private val _tasks: MutableLiveData<List<Task>> = MutableLiveData()
    val tasks: LiveData<List<Task>>
        get() = _tasks

    private val _currentFilter: MutableLiveData<TasksActivityFilter> =
        MutableLiveData(TasksActivityFilter.ALL)

    private val _currentFilterMenuItemId: MutableLiveData<Int> =
        MutableLiveData(R.id.mnuFilterAll)
    val currentFilterMenuItemId: LiveData<Int>
        get() = _currentFilterMenuItemId

    private val _activityTitle: MutableLiveData<String> =
        MutableLiveData(application.getString(R.string.tasks_title_all))
    val activityTitle: LiveData<String>
        get() = _activityTitle

    private val _lblEmptyViewText: MutableLiveData<String> =
        MutableLiveData(application.getString(R.string.tasks_no_tasks_yet))
    val lblEmptyViewText: LiveData<String>
        get() = _lblEmptyViewText

    // Eventos de comunicación con la actividad

    private val _onStartActivity: MutableLiveData<Event<Intent>> = MutableLiveData()
    val onStartActivity: LiveData<Event<Intent>>
        get() = _onStartActivity

    private val _onShowMessage: MutableLiveData<Event<String>> = MutableLiveData()
    val onShowMessage: LiveData<Event<String>>
        get() = _onShowMessage

    private val _onShowTaskDeleted: MutableLiveData<Event<Task>> = MutableLiveData()
    val onShowTaskDeleted: LiveData<Event<Task>>
        get() = _onShowTaskDeleted

    init {

        _tasks.value = repository.queryAllTasks().toList()

    }

    var lista: List<Task> = _tasks.value as List<Task>

    // ACTION METHODS

    // Hace que se muestre en el RecyclerView todas las tareas.
    fun filterAll() {

        lista = repository.queryAllTasks()

        _currentFilter.value = TasksActivityFilter.ALL

        _currentFilterMenuItemId.value = R.id.mnuFilterAll

        _tasks.value = lista.sortedByDescending { it.id }

    }

    // Hace que se muestre en el RecyclerView sólo las tareas completadas.
    fun filterCompleted() {

       lista = repository.queryCompletedTasks()

        _currentFilter.value = TasksActivityFilter.COMPLETED

        _currentFilterMenuItemId.value = R.id.mnuFilterCompleted

        _tasks.value = lista.sortedByDescending { it.id }

    }

    // Hace que se muestre en el RecyclerView sólo las tareas pendientes.
    fun filterPending() {

        lista = repository.queryPendingTasks()

        _currentFilter.value = TasksActivityFilter.PENDING

        _currentFilterMenuItemId.value = R.id.mnuFilterPending

        _tasks.value = lista.sortedByDescending { it.id }

    }

    // Agrega una nueva tarea con dicho concepto. Si la se estaba mostrando
    // la lista de solo las tareas completadas, una vez agregada se debe
    // mostrar en el RecyclerView la lista con todas las tareas, no sólo
    // las completadas.
    fun addTask(concept: String) {

        repository.addTask(concept)

        if (lista == repository.queryCompletedTasks()) {

            filterAll()

        }

        else {

            queryTasks(_currentFilter.value!!)

        }



    }

    // Agrega la tarea
    fun insertTask(task: Task) {

        repository.insertTask(task)

        queryTasks(_currentFilter.value!!)
    }

    // Borra la tarea
    fun deleteTask(task: Task) {

        repository.deleteTask(task.id)

        queryTasks(_currentFilter.value!!)

    }

    // Borra todas las tareas mostradas actualmente en el RecyclerView.
    // Si no se estaba mostrando ninguna tarea, se muestra un mensaje
    // informativo en un SnackBar de que no hay tareas que borrar.
    fun deleteTasks() {

            var toDelete = _tasks.value!!.map { it.id }

            repository.deleteTasks(toDelete)

            queryTasks(_currentFilter.value!!)


    }

    // Marca como completadas todas las tareas mostradas actualmente en el RecyclerView,
    // incluso si ya estaban completadas.
    // Si no se estaba mostrando ninguna tarea, se muestra un mensaje
    // informativo en un SnackBar de que no hay tareas que marcar como completadas.
    fun markTasksAsCompleted() {

            var toComplete = _tasks.value!!.map { it.id }

            repository.markTasksAsCompleted(toComplete)

            queryTasks(_currentFilter.value!!)
    }

    // Marca como pendientes todas las tareas mostradas actualmente en el RecyclerView,
    // incluso si ya estaban pendientes.
    // Si no se estaba mostrando ninguna tarea, se muestra un mensaje
    // informativo en un SnackBar de que no hay tareas que marcar como pendientes.
    fun markTasksAsPending() {

        var toPending = _tasks.value!!.map { it.id }

        repository.markTasksAsPending(toPending)

        queryTasks(_currentFilter.value!!)
    }

    // Hace que se envíe un Intent con la lista de tareas mostradas actualmente
    // en el RecyclerView.
    // Si no se estaba mostrando ninguna tarea, se muestra un Snackbar indicando
    // que no hay tareas que compartir.
    fun shareTasks() {



    }

    // Actualiza el estado de completitud de la tarea recibida, atendiendo al
    // valor de isCompleted. Si es true la tarea es marcada como completada y
    // en caso contrario es marcada como pendiente.
    fun updateTaskCompletedState(task: Task, isCompleted: Boolean) {

        if (isCompleted) {

            repository.markTaskAsCompleted(task.id)

        }

        else {

            repository.markTaskAsPending(task.id)

        }

        queryTasks(_currentFilter.value!!)

    }

    // Retorna si el concepto recibido es válido (no es una cadena vacía o en blanco)
    fun isValidConcept(concept: String): Boolean = concept.isNotBlank()

    // Pide las tareas al repositorio, atendiendo al filtro recibido
    private fun queryTasks(filter: TasksActivityFilter) {
        if (filter == TasksActivityFilter.PENDING) {

            filterPending()

        }

        else if (filter == TasksActivityFilter.COMPLETED) {

            filterCompleted()

        }

        else {

            filterAll()

        }
    }

}

