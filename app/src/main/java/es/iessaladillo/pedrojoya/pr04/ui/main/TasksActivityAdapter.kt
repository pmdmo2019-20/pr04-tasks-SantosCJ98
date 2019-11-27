package es.iessaladillo.pedrojoya.pr04.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.pedrojoya.pr04.R
import es.iessaladillo.pedrojoya.pr04.data.entity.Task
import es.iessaladillo.pedrojoya.pr04.utils.strikeThrough
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.tasks_activity_item.*

// TODO: Crea una clase TasksActivityAdapter que actúe como adaptador del RecyclerView
//  y que trabaje con una lista de tareas.
//  Cuando se haga click sobre un elemento se debe cambiar el estado de completitud
//  de la tarea, pasando de completada a pendiente o viceversa.
//  La barra de cada elemento tiene un color distinto dependiendo de si la tarea está
//  completada o no.
//  Debajo del concepto se muestra cuando fue creada la tarea, si la tarea está pendiente,
//  o cuando fue completada si la tarea ya ha sido completada.
//  Si la tarea está completada, el checkBox estará chequeado y el concepto estará tachado.

class TasksActivityAdapter : RecyclerView.Adapter<TasksActivityAdapter.ViewHolder>() {

    init {

        setHasStableIds(true)

    }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val itemView = layoutInflater.inflate(R.layout.tasks_activity_item, parent, false)

        return ViewHolder(itemView, onItemClickListener)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(data[position])

    }

    fun submitList(newList: List<Task>) {

        data = newList

        notifyDataSetChanged()



    }

    private var data: List<Task> = emptyList()

    init {

        setHasStableIds(true)

    }

    override fun getItemId(position: Int): Long {

        return data[position].id

    }

    interface OnItemClickListener {

        fun onItemClick(position: Int)

    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {

        this.onItemClickListener = onItemClickListener

    }



    fun getItem(position: Int) = data[position]

    class ViewHolder(override val containerView: View, onItemClickListener: OnItemClickListener?) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {

            itemView.setOnClickListener {

                val position = adapterPosition

                if (position != RecyclerView.NO_POSITION) {

                    onItemClickListener?.onItemClick(position)

                }

            }

            chkCompleted.setOnClickListener {

                val position = adapterPosition

                if (position != RecyclerView.NO_POSITION) {

                    onItemClickListener?.onItemClick(position)

                }

            }

        }


        fun bind(task: Task) {

            lblConcept.text = task.concept

            chkCompleted.setChecked(task.completed)

            if (chkCompleted.isChecked) {

                lblConcept.strikeThrough(true)

                viewBar.setBackgroundResource(R.color.colorCompletedTask)

                lblCompleted.text = task.completedAt

                task.completed = true

                //TODO

            }

            else {

                lblConcept.strikeThrough(false)

                viewBar.setBackgroundResource(R.color.colorPendingTask)

                lblCompleted.text = task.createdAt

                task.completed = false

                //TODO

            }



        }

    }
}


