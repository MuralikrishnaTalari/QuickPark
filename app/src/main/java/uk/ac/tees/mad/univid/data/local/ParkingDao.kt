package uk.ac.tees.mad.univid.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.ac.tees.mad.univid.models.ParkingSpot

@Dao
interface ParkingDao {

    @Query("SELECT * from parking_spots")
    suspend fun getAllParkingSpots(): List<ParkingSpot>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParkingSpot(parkingSpot: ParkingSpot)

    @Delete
    suspend fun deleteParkingSpot(parkingSpot: ParkingSpot)

    @Query("SELECT * FROM parking_spots WHERE name = :name")
    suspend fun getParkingSpotByName(name: String): ParkingSpot?

}