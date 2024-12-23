package uk.ac.tees.mad.univid.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.univid.R
import uk.ac.tees.mad.univid.viewmodel.MainViewModel

@Composable
fun FavoriteScreen(navController: NavHostController, viewModel: MainViewModel) {
    val parkingSpots = viewModel.savedParkingSpot
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
    ){
        if (parkingSpots.value!=null){
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(parkingSpots.value!!){
                Card(modifier = Modifier.fillMaxWidth().padding(20.dp)) {

                }
            }
            }
        }else{
            Text(text = "No Data Found", modifier = Modifier.align(Alignment.Center))
        }
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController, selectedIcon = bottomNavItems.Favorite)
        }
    }
}