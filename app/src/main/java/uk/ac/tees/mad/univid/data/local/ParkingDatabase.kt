package uk.ac.tees.mad.univid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.ac.tees.mad.univid.models.ParkingSpot

@Database(entities = [ParkingSpot::class], version = 1, exportSchema = false)
abstract class ParkingDatabase :RoomDatabase() {
    abstract fun parkingDao(): ParkingDao
}