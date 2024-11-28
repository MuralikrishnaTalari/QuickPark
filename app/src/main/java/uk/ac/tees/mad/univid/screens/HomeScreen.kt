package uk.ac.tees.mad.univid.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import uk.ac.tees.mad.univid.viewmodel.MainViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import uk.ac.tees.mad.univid.ParkingNavigation

@Composable
fun HomeScreen(navController: NavHostController, viewModel: MainViewModel) {
    val parkingSpots = viewModel.parkingSpots
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(LatLng(51.5074,-0.1278), 10f)
    }
    GoogleMap(modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState){
        parkingSpots.value.forEach { spot ->
            Marker(
                state = MarkerState(position = LatLng(spot.latitude.toDouble(), spot.longitude.toDouble())),
                title = spot.name,
                snippet = if (spot.available) "Available" else "Not Available",
                onInfoWindowClick = {
                    navController.navigate(ParkingNavigation.DetailScreen.createRoute(spot))
                }
            )
        }
    }
}