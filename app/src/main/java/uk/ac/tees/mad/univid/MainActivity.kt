package uk.ac.tees.mad.univid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.univid.screens.HomeScreen
import uk.ac.tees.mad.univid.screens.SignIN
import uk.ac.tees.mad.univid.screens.SignUP
import uk.ac.tees.mad.univid.screens.SplashScreen
import uk.ac.tees.mad.univid.ui.theme.QuickParkTheme
import uk.ac.tees.mad.univid.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuickParkTheme {
                parkingApp()
            }
        }
    }
}

sealed class ParkingNavigation(val route : String){
    object SplashScreen : ParkingNavigation("splash_screen")
    object LoginScreen : ParkingNavigation("login_screen")
    object SignUpScreen : ParkingNavigation("signup_screen")
    object HomeScreen : ParkingNavigation("home_screen")
}

@Composable
fun parkingApp(){
    val navController = rememberNavController()
    val viewModel : MainViewModel = viewModel()
    Surface() {
        NavHost(
            navController = navController,
            startDestination = ParkingNavigation.SplashScreen.route
        ) {
            composable(ParkingNavigation.SplashScreen.route) {
                SplashScreen(navController = navController, viewModel = viewModel)
            }
            composable(ParkingNavigation.LoginScreen.route) {
                SignIN(navController = navController, viewModel = viewModel)
            }
            composable(ParkingNavigation.SignUpScreen.route) {
                SignUP(navController = navController, viewModel = viewModel)
            }
            composable(ParkingNavigation.HomeScreen.route) {
                HomeScreen(navController = navController, viewModel = viewModel)
            }
        }
    }
}