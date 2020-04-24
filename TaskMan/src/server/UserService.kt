package server

import model.User
import java.util.UUID.randomUUID


class UserService{
    private val users = mutableListOf<User>()


    fun register(auth: Boolean, user: User): String = when {
        !auth -> {
            user.id = randomUUID().toString()
            require(user.username !in users.map { it.username })
            this.users.add(user)
            user.toString()
        }
        else -> "You are already authentified"
    }


    fun login(auth: Boolean, user: User): String = when {
        !auth -> {
            user.id = randomUUID().toString()
            require(user.username in users.map { it.username })
            "User logged in "
        }
        else -> "You are already authentified"
    }
}