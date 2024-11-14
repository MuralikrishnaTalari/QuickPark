package uk.ac.tees.mad.univid.models

data class UserData(
    val uid : String = "",
    val name : String = "",
    val profilePicture : String = "",
    val email : String = "",
    val password : String = ""
)
