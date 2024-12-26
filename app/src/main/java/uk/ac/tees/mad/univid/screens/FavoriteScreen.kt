package uk.ac.tees.mad.univid.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import uk.ac.tees.mad.univid.R
import uk.ac.tees.mad.univid.ui.theme.afacadflux
import uk.ac.tees.mad.univid.ui.theme.poppins
import uk.ac.tees.mad.univid.viewmodel.MainViewModel

@Composable
fun FavoriteScreen(navController: NavHostController, viewModel: MainViewModel) {
    val parkingSpots = viewModel.savedParkingSpot
    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {

        } else {
            Toast.makeText(context, "Permission denied to make a call", Toast.LENGTH_SHORT).show()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        if (parkingSpots.value != null) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(parkingSpots.value!!) { items ->
                    Box {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                                .shadow(20.dp, RoundedCornerShape(20.dp))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = items.name,
                                    fontFamily = poppins,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 30.sp,
                                    color = colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = getAddressFromLatLng(
                                        context = context,
                                        items.latitude.toDouble(),
                                        items.longitude.toDouble()
                                    ),
                                    fontFamily = afacadflux, fontSize = 20.sp,
                                    color = colorScheme.onSurface
                                )
                                Text(
                                    text = "Price: ${items.price}",
                                    fontFamily = afacadflux,
                                    fontSize = 20.sp,
                                    color = colorScheme.onSurface
                                )
                                Text(
                                    text = "Available: ${items.available}",
                                    fontFamily = afacadflux,
                                    fontSize = 20.sp,
                                    color = colorScheme.onSurface
                                )
                                Text(
                                    text = "Owner: ${items.ownerName}",
                                    fontFamily = afacadflux,
                                    fontSize = 20.sp,
                                    color = colorScheme.onSurface
                                )
                                Button(onClick = {
                                    if (ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.CALL_PHONE
                                        )
                                        != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                                    } else {
                                        makePhoneCall(context, items.ownerName.toString())
                                    }
                                }) {
                                    Text(text = "Contact Owner")
                                }
                            }
                        }
                        Box(modifier = Modifier.padding(40.dp).size(40.dp).align(Alignment.BottomEnd).clip(
                            CircleShape
                        ).background(Color.White.copy(alpha = 0.6f)).clickable {
                            viewModel.deleteParkingSpot(items)
                        }){
                            Icon(imageVector = Icons.Outlined.Delete, contentDescription = null,modifier = Modifier.align(
                                Alignment.Center), tint = Color.Red)
                        }
                    }
                }
            }
        } else {
            Text(text = "No Data Found", modifier = Modifier.align(Alignment.Center))
        }
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController, selectedIcon = bottomNavItems.Favorite)
        }
    }
}