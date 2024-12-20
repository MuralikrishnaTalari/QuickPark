package uk.ac.tees.mad.univid

import ProfileScreen
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import uk.ac.tees.mad.univid.models.ParkingSpot
import uk.ac.tees.mad.univid.screens.DetailScreen
import uk.ac.tees.mad.univid.screens.FavoriteScreen
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

sealed class ParkingNavigation(val route: String) {
    object SplashScreen : ParkingNavigation("splash_screen")
    object LoginScreen : ParkingNavigation("login_screen")
    object SignUpScreen : ParkingNavigation("signup_screen")
    object HomeScreen : ParkingNavigation("home_screen")
    object DetailScreen : ParkingNavigation("details_screen/{item}") {
        fun createRoute(spot: ParkingSpot): String {
            val itemJson = Uri.encode(Json.encodeToString(spot))
            return "details_screen/$itemJson"
        }
    }
    object FavoriteScreen : ParkingNavigation("favorite_screen")
    object ProfileScreen : ParkingNavigation("profile_screen")
}

@Composable
fun parkingApp() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()
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
            composable(
                route = ParkingNavigation.DetailScreen.route,
                arguments = listOf(navArgument("item") { type = NavType.StringType })
            ) { backStackEntry ->
                val itemJson = backStackEntry.arguments?.getString("item")
                val parkingSpot = itemJson?.let { Json.decodeFromString<ParkingSpot>(it) }
                parkingSpot?.let {
                    Log.d("DetailScreen", "Parking Spot: $it")
                    DetailScreen(spot = it, navController = navController, viewModel = viewModel)
                }
            }
            composable(ParkingNavigation.FavoriteScreen.route) {
                FavoriteScreen()
            }
            composable(ParkingNavigation.ProfileScreen.route) {
                ProfileScreen(navController = navController, viewModel = viewModel)
            }
        }
    }
}

