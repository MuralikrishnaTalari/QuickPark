import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.Space
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import uk.ac.tees.mad.univid.ParkingNavigation
import uk.ac.tees.mad.univid.R
import uk.ac.tees.mad.univid.screens.BottomNavBar
import uk.ac.tees.mad.univid.screens.bottomNavItems
import uk.ac.tees.mad.univid.viewmodel.MainViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(navController: NavHostController, viewModel: MainViewModel) {
    val loading = viewModel.loading
    val context = LocalContext.current
    val user = viewModel.userDetails
    val name = remember { mutableStateOf(user.value.name ?: "") }
    val email = remember { mutableStateOf(user.value.email ?: "") }

    val photoUri = remember { mutableStateOf<Uri?>(null) }
    val imageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                photoUri.value?.let {
                    viewModel.uploadProfilePhoto(it)
                }
            } else {
                Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = createUriForImage(context)
            photoUri.value = uri
            imageLauncher.launch(uri!!)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                if (user.value.profilePicture.isNotEmpty()) {
                    AsyncImage(
                        model = user.value.profilePicture,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(15.dp)
                    )
                    AsyncImage(
                        model = user.value.profilePicture,
                        contentDescription = null,
                        modifier = Modifier
                            .statusBarsPadding()
                            .size(100.dp)
                            .clip(CircleShape)
                            .align(Alignment.Center)
                            .border(
                                1.dp,
                                color = androidx.compose.ui.graphics.Color.White,
                                CircleShape
                            )
                            .clickable {
                                if (ContextCompat.checkSelfPermission(
                                        context, Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    val uri = createUriForImage(context)
                                    photoUri.value = uri
                                    imageLauncher.launch(uri!!)
                                } else {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.male),
                        contentDescription = null,
                        modifier = Modifier
                            .statusBarsPadding()
                            .size(100.dp)
                            .clip(CircleShape)
                            .align(Alignment.Center)
                            .border(
                                1.dp,
                                color = androidx.compose.ui.graphics.Color.White,
                                CircleShape
                            )
                            .clickable {
                                if (ContextCompat.checkSelfPermission(
                                        context, Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    val uri = createUriForImage(context)
                                    photoUri.value = uri
                                    imageLauncher.launch(uri!!)
                                } else {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    TextField(
                        value = name.value, onValueChange = { name.value = it },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    Icon(
                        imageVector = Icons.Sharp.Edit,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    TextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    Icon(
                        imageVector = Icons.Sharp.Edit,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = { viewModel.updateUserDetails(name.value) }, shape = RoundedCornerShape(20.dp),modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp)) {
                    Text(text = "Save")
                }
                Button(onClick = {
                    navController.navigate(ParkingNavigation.LoginScreen.route){
                        popUpTo(0)
                    }
                    viewModel.signOut() }, shape = RoundedCornerShape(20.dp),modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp)) {
                    Text(text = "Logout")
                }
            }

        }
        if (loading.value){
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController, selectedIcon = bottomNavItems.Profile)
        }
    }
}

private fun createUriForImage(context: Context): Uri? {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)

    return try {
        FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        null
    }
}
