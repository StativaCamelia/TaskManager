package model


data class User(var id: String? = null, val username: String, val type: Type?=Type.NORMAL_USER){

    enum class Type{
        ADMIN,
        NORMAL_USER
    }

}
