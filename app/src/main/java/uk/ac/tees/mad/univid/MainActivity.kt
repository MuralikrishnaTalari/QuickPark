package uk.ac.tees.mad.univid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.univid.screens.SplashScreen
import uk.ac.tees.mad.univid.ui.theme.QuickParkTheme

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
}

@Composable
fun parkingApp(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ParkingNavigation.SplashScreen.route) {
        composable(ParkingNavigation.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
    }
}