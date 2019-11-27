package es.iessaladillo.pedrojoya.pr04.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.iessaladillo.pedrojoya.pr04.data.Repository
import java.lang.IllegalArgumentException

// TODO: Crea una clase TasksActivityViewModelFactory, que implemente ViewModelProvider.Factory
//  para construir un objeto TasksActivityViewModel

class TasksActivityViewModelFactory(private val repository: Repository, val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(TasksActivityViewModel::class.java)) {

            return TasksActivityViewModel(repository, application) as T

        }

        else {

            throw IllegalArgumentException("Error en ViewModel")

        }

    }


}
