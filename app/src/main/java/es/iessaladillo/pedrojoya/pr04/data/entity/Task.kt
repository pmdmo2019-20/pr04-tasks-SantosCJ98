package es.iessaladillo.pedrojoya.pr04.data.entity

// TODO: Crea una clase llamada Task con las siguientes propiedades:
//  iDs (Long), concept(String), createdAt (String),
//  completed (Boolean), completedAt (String)


data class Task (var id: Long, var concept: String, var createdAt: String, var completed: Boolean,
                 var completedAt: String)

