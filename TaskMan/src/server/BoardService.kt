package server


import model.Board
import java.util.UUID.randomUUID


class BoardService(service: TaskService) {
    private val boards = mutableListOf<Board>()
    private var taskService: TaskService = service

    fun addBoard(auth: Boolean, board: Board): String = when {
        auth -> {
            board.id = randomUUID().toString()
            require(board.name !in boards.map { it.name })
            boards.add(board)
            boards.toString()
        }
        else -> "You should be authenticated (sign in) in order to perform this operation"
    }

    fun addTaskToBoard(auth: Boolean, taskName: String, boardName: String): String {
        when {
            !auth -> return "You should be authenticated (sign in) in order to perform this operation"
            boardName !in boards.map { it.name } -> return "The board doesn't exist. Make sure to create the board first"
            else -> {
                var board = boards.find { it.name == boardName }
                val task = this.taskService.findTaskByName(taskName)
                if(task != null) {
                    if (board != null) {
                        board.tasks!!.add(task)
                        return board.toString()
                    }
                }
                return "Task doesn't exist"
            }
        }
    }
}