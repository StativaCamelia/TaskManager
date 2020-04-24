package model


import kotlin.collections.ArrayList

data class Board(var id: String? = null, val name: String, var tasks: ArrayList<Task> ? =null){
}
