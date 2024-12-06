package uk.ac.tees.mad.univid.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.univid.ParkingNavigation
import uk.ac.tees.mad.univid.R
import uk.ac.tees.mad.univid.ui.theme.afacadflux

sealed class bottomNavItems(val route: String, val icon: ImageVector, val title: String) {
    object Profile :
        bottomNavItems(ParkingNavigation.ProfileScreen.route, Icons.Outlined.Person, "Profile")

    object Home : bottomNavItems(ParkingNavigation.HomeScreen.route, Icons.Outlined.Home, "Home")
    object Favorite :
        bottomNavItems(ParkingNavigation.FavoriteScreen.route, Icons.Outlined.FavoriteBorder, "Favorite")
}

@Composable
fun BottomNavBar(navController: NavHostController, selectedIcon: bottomNavItems) {
    val items = listOf(
        bottomNavItems.Profile,
        bottomNavItems.Home,
        bottomNavItems.Favorite
    )
    Box {
        Row {
            for (i in items) {
                Column(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(60.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (i == selectedIcon) colorScheme.primary else Color.White.copy(alpha = 0.5f))
                        .clickable { navController.navigate(i.route) },
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = i.icon,
                        contentDescription = null,
                        tint = if (i == selectedIcon) Color.White else Color.Black,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(30.dp)
                    )
                    Text(
                        text = i.title,
                        fontFamily = afacadflux,
                        color = if (i == selectedIcon) Color.White else Color.Black,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}


