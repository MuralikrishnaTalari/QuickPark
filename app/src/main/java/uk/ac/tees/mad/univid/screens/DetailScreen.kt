package uk.ac.tees.mad.univid.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.KeyboardBackspace
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.tees.mad.univid.models.ParkingSpot
import uk.ac.tees.mad.univid.ui.theme.afacadflux
import uk.ac.tees.mad.univid.viewmodel.MainViewModel
import java.io.IOException
import java.util.Locale

@Composable
fun DetailScreen(spot: ParkingSpot, navController: NavHostController, viewModel: MainViewModel) {
    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            makePhoneCall(context, spot.ownerNumber.toString())
        } else {
            Toast.makeText(context, "Permission denied to make a call", Toast.LENGTH_SHORT).show()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                    .background(colorScheme.primary)
            ) {}
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(30.dp)
                .shadow(40.dp, RoundedCornerShape(20.dp))
                .clip(
                    RoundedCornerShape(20.dp)
                )
                .background(colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = spot.name,
                fontFamily = afacadflux,
                fontSize = 34.sp,
                color = colorScheme.onSurface
            )
            Text(
                text = getAddressFromLatLng(
                    context,
                    spot.latitude.toDouble(),
                    spot.longitude.toDouble()
                ), fontFamily = afacadflux, fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 25.dp), color = colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Owner: ${spot.ownerName}",
                fontFamily = afacadflux,
                fontSize = 30.sp,
                color = colorScheme.onSurface
            )
            Text(
                text = "Price: ${spot.price}",
                fontFamily = afacadflux,
                fontSize = 30.sp,
                color = colorScheme.onSurface
            )
            Text(
                text = if (spot.available) "Available" else "Not Available",
                fontFamily = afacadflux,
                fontSize = 30.sp,
                color = if (spot.available) Color.Green else Color.Red
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                } else {
                    makePhoneCall(context, spot.ownerNumber.toString())
                }
            }) {
                Icon(
                    imageVector = Icons.Outlined.Call,
                    contentDescription = "call owner",
                    tint = colorScheme.onSurface
                )
                Text(
                    text = "Contact Owner",
                    fontFamily = afacadflux,
                    color = colorScheme.onSurface,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = { viewModel.insertParkingSpot(spot) }) {
                Text(
                    text = "Save to Favorite",
                    fontFamily = afacadflux,
                    color = colorScheme.onSurface,
                    fontSize = 20.sp
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(start = 25.dp, top = 40.dp)
                .size(50.dp)
                .clip(
                    CircleShape
                )
                .background(Color.LightGray.copy(alpha = 0.6f))
                .clickable { navController.popBackStack() }

        ) {
            Icon(
                imageVector = Icons.Outlined.KeyboardBackspace,
                contentDescription = "back",
                tint = Color.Black,
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

private fun makePhoneCall(context: android.content.Context, phoneNumber: String) {
    val callIntent = Intent(Intent.ACTION_CALL)
    callIntent.data = Uri.parse("tel:$phoneNumber")
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        context.startActivity(callIntent)
    } else {
        Toast.makeText(context, "Permission denied to make a call", Toast.LENGTH_SHORT).show()
    }
}

fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    return try {
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        if (!addresses.isNullOrEmpty()) {
            val address: Address = addresses[0]
            address.getAddressLine(0) ?: "No address found"
        } else {
            "No address found"
        }
    } catch (e: IOException) {
        e.printStackTrace()
        "Unable to get the address"
    }
}