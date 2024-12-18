package uk.ac.tees.mad.univid.models
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "parking_spots")
@Serializable
data class ParkingSpot(
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
    val name: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val available: Boolean = true,
    val price: Int = 0,
    val ownerName: String = "",
    val ownerNumber: Long = 0,
)
