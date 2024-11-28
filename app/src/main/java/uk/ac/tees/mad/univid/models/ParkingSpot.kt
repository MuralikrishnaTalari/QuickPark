package uk.ac.tees.mad.univid.models
import kotlinx.serialization.Serializable

@Serializable
data class ParkingSpot(
    val name: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val available: Boolean = true,
    val price: Int = 0,
    val ownerName: String = "",
    val ownerNumber: Long = 0,
)
