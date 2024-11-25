package uk.ac.tees.mad.univid.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.univid.viewmodel.MainViewModel

@Composable
fun HomeScreen(navController: NavHostController, viewModel: MainViewModel) {
    Text(text = "Home Screen", fontSize = 40.sp)
}