package uk.ac.tees.mad.univid.data.dependencies

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.univid.data.local.ParkingDao
import uk.ac.tees.mad.univid.data.local.ParkingDatabase

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    fun providesAuthentication(): FirebaseAuth = Firebase.auth

    @Provides
    fun providesFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun providesStorage(): FirebaseStorage = Firebase.storage

    @Provides
    fun provideParkingSpotDao(database: ParkingDatabase): ParkingDao {
        return database.parkingDao()
    }

    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ParkingDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ParkingDatabase::class.java,
            "parking_spot_database"
        ).fallbackToDestructiveMigration()
            .build()
    }
}