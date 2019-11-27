package es.iessaladillo.pedrojoya.pr04.data

import es.iessaladillo.pedrojoya.pr04.data.entity.Task
import java.text.SimpleDateFormat
import java.util.*

// TODO: Crea una clase llamada LocalRepository que implemente la interfaz Repository
//  usando una lista mutable para almacenar las tareas.
//  Los iDs de las tareas se irán generando secuencialmente a partir del valor 1 conforme
//  se van agregando tareas (add).

var iDs: Long = 0

val sdf = SimpleDateFormat("dd/MM/yyyy, hh:mm:ss")

object LocalRepository : Repository {


    var tasks: MutableList<Task> = mutableListOf()

    override fun queryAllTasks(): List<Task> {

        return tasks

    }

    override fun queryCompletedTasks(): List<Task> = tasks.filter { it.completed }

    override fun queryPendingTasks(): List<Task> = tasks.filter { !it.completed }

    override fun addTask(concept: String) {

        val task = Task(iDs, concept, sdf.format(Date()), false, "")

        tasks.add(task)

        iDs++

    }

    override fun insertTask(task: Task) {

        tasks.add(task)
    }

    override fun deleteTask(taskId: Long) {

        tasks.removeAll{it.id == taskId}
    }

    override fun deleteTasks(taskIdList: List<Long>) {

        var toDelete = tasks.filter { taskIdList.contains(it.id)  }

        tasks.removeAll(toDelete)

    }

    override fun markTaskAsCompleted(taskId: Long) {

        tasks.filter { taskId == it.id  }.forEach{

            it.completed = true

            it.completedAt = sdf.format(Date())

        }


    }

    override fun markTasksAsCompleted(taskIdList: List<Long>) {

        tasks.filter { taskIdList.contains(it.id)  }.forEach{

            it.completed = true

            it.completedAt = sdf.format(Date())

        }


    }

    override fun markTaskAsPending(taskId: Long) {

        tasks.filter { taskId == it.id  }.forEach{

            it.completed = false

            it.completedAt = ""

        }
    }

    override fun markTasksAsPending(taskIdList: List<Long>) {

        tasks.filter { taskIdList.contains(it.id)  }.forEach{

            it.completed = false

            it.completedAt = ""

        }
    }


}

