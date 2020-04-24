package server
import model.Board
import model.Task
import model.User
import java.io.OutputStream
import java.lang.UnsupportedOperationException
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.thread
fun main(args: Array<String>) {
    val server = ServerSocket(9999)
    println("Server is running on port ${server.localPort}")
    while (true) {
        val client = server.accept()
        println("Client connected: ${client.inetAddress.hostAddress}")
        // Run client in it's own thread.
        thread { ClientHandler(client).run() }
    }
}

class ClientHandler(
    private val client: Socket,
    private val reader: Scanner,
    private val writer: OutputStream
) {
    constructor(client: Socket): this(
        client = client,
        reader = Scanner(client.getInputStream()),
        writer = client.getOutputStream()
    )
    private val taskService: TaskService = TaskService()
    private val userService: UserService = UserService()
    private val boardService: BoardService = BoardService(taskService)

    private var running: Boolean = false
    private var auth: Boolean = false;

    fun run() {
        running = true
        write(
            """
                |Welcome to the server!
                |To Exit, write: 'EXIT'.
                | - register <USERNAME><TYPE>
                | - login <USERNAME>
                | - create_board <BOARD name>
                | - add <TASK name> <TASK priority> <TASK description> <TASK type> <TASK estimatedTime>
                | - add_to_board <Task name><BOARD name>
                | - list_all
                | - list_by_status <TASK status>
                | - list_by_type <TASK type>
                | - list_by_user <Username>
                | - assign <TASK name> <User>
                | - change_status <TASK name> <Status>
                | - delete_task <TASK name>
                | User types : ADMIN, NORMAL_USER
                | Available priorities : URGENT,IMPORTANT,NOT_IMPORTANT
                | Available statuses : TO_DO,DOING,IN_REVIEW, DONE
                | Available Types: DEV, TEST, REVIEW
            """.trimMargin()
        )
        while (running) {
            try {
                val text = reader.nextLine()
                if (text == "EXIT"){
                    shutdown()
                    continue
                }
                val values = text.split(' ')
                val result: String;
                    when(values[0]) {
                        "add" -> {
                            result = taskService.addTask(auth, Task(title = values[1], priority = Task.Priority.valueOf(values[2]),descriptions = values[3], taskType = Task.TaskTypes.valueOf(values[4]), estimatedTime = values[5].toInt()))
                        }
                        "register" ->{
                            result = userService.register(auth, User(username = values[1], type=User.Type.valueOf(values[2])))
                            auth = true;
                        }
                        "login" ->{
                            result = userService.login(auth, User(username = values[1]))
                            auth = true;
                        }
                        "create_board" ->{
                            result = boardService.addBoard(auth, Board(name = values[1]))
                        }
                        "list_all" ->{
                            result = taskService.listAll(auth)
                        }
                        "list_by_status" ->{
                            result = taskService.listByStatus(auth, status = Task.Status.valueOf(values[1]))
                        }
                        "list_by_type" ->{
                            result = taskService.listByType(auth, type = Task.TaskTypes.valueOf(values[1]))
                        }
                        "list_by_user" ->{
                            result = taskService.listByUser(auth, assignedTo = values[1])
                        }
                        "assign" ->{
                            result = taskService.assignTask(auth, taskName = values[1], assignTo = values[2])
                        }
                        "change_status" ->{
                            result = taskService.changeStatus(auth, taskName = values[1],newStatus = enumValueOf(values[2]))
                        }
                        "delete_task" ->{
                            result = taskService.deleteTask(auth, taskName = values[1])
                        }
                        "add_to_board" ->{
                            result = boardService.addTaskToBoard(auth, taskName = values[1], boardName = values[2])
                        }
                       else -> throw UnsupportedOperationException("Operation not supported!")
                    }
                write(result)
            } catch (ex: Exception) {
                println(ex.message)
                shutdown()
            }
        }
    }
    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }
    private fun shutdown() {
        running = false
        client.close()
        println("${client.inetAddress.hostAddress} closed the connection")
    }
}