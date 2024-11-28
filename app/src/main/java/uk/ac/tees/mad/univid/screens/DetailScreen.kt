package uk.ac.tees.mad.univid.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.univid.models.ParkingSpot

@Composable
fun DetailScreen(spot: ParkingSpot) {
    Text(text = spot.name, fontSize = 20.sp, color = Color.Black)
    Text(text = spot.latitude)
}