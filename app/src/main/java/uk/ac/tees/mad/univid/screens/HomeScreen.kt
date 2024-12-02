package uk.ac.tees.mad.univid.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
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
import uk.ac.tees.mad.univid.models.ParkingSpot

@Composable
fun HomeScreen(navController: NavHostController, viewModel: MainViewModel) {
    val search = remember {
        mutableStateOf("")
    }
    val parkingSpots = viewModel.parkingSpots
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(51.5074, -0.1278), 10f)
    }
    val ShowDataType = remember {
        mutableStateOf("Idle")
    }
    val searchedData = remember {
        mutableStateOf<ParkingSpot?>(null)
    }
    val searchCameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(51.5074, -0.1278), 10f)
    }
    if (searchedData.value!=null){
        searchCameraState.position = CameraPosition.fromLatLngZoom(LatLng(searchedData.value!!.latitude.toDouble(), searchedData.value!!.longitude.toDouble()), 10f)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()) {
        when (ShowDataType.value) {
            "Idle" -> GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                parkingSpots.value.forEach { spot ->
                    Marker(
                        state = MarkerState(
                            position = LatLng(
                                spot.latitude.toDouble(),
                                spot.longitude.toDouble()
                            )
                        ),
                        title = spot.name,
                        snippet = if (spot.available) "Available" else "Not Available",
                        onInfoWindowClick = {
                            navController.navigate(ParkingNavigation.DetailScreen.createRoute(spot))
                        }
                    )
                }
            }

            "Search" -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (searchedData.value != null) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = searchCameraState
                        ) {
                            Marker(
                                state = MarkerState(
                                    position = LatLng(
                                        searchedData.value!!.latitude.toDouble(),
                                        searchedData.value!!.longitude.toDouble()
                                    )
                                ),
                                title = searchedData.value!!.name,
                                snippet = if (searchedData.value!!.available) "Available" else "Not Available",
                                onInfoWindowClick = {
                                    navController.navigate(
                                        ParkingNavigation.DetailScreen.createRoute(
                                            searchedData.value!!
                                        )
                                    )
                                }
                            )
                        }
                    } else {
                        Text(text = "No Data Found", modifier = Modifier.align(Alignment.Center))
                    }
                    Icon(
                        imageVector = Icons.Rounded.Restore,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 120.dp, end = 20.dp)
                            .size(40.dp)
                            .align(Alignment.TopEnd)
                            .clickable {
                                searchedData.value = null
                                ShowDataType.value = "Idle"
                            },
                        tint = Color.Red
                    )
                }
            }
        }
        OutlinedTextField(
            value = search.value,
            onValueChange = { search.value = it },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White.copy(alpha = 0.7f),
                textColor = Color.Black,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                cursorColor = Color.Black,
                focusedLabelColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .statusBarsPadding(),
            shape = RoundedCornerShape(20.dp),
            label = { Text(text = "Search for Parking Places") },
            trailingIcon = {
                Icon(imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable {
                            searchedData.value = searchForItem(parkingSpots.value, search.value)
                            ShowDataType.value = "Search"
                        })
            },
            singleLine = true
        )
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController, selectedIcon = bottomNavItems.Home)
        }
    }
}



fun searchForItem(itemList: List<ParkingSpot>, searchingFor: String): ParkingSpot? {
    itemList.forEach {
        if (it.name.contains(searchingFor, ignoreCase = true)) {
            return it
        }
    }
    return null
}