package uk.ac.tees.mad.univid.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.univid.ParkingNavigation
import uk.ac.tees.mad.univid.ui.theme.afacadflux
import uk.ac.tees.mad.univid.ui.theme.poppins
import uk.ac.tees.mad.univid.viewmodel.MainViewModel

@Composable
fun SignUP(navController: NavHostController, viewModel: MainViewModel) {
    val name = remember {
        mutableStateOf("")
    }
    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val loading = viewModel.loading
    val signed = viewModel.signed
    LaunchedEffect(key1 = signed.value) {
        if (signed.value) {
            navController.navigate(ParkingNavigation.HomeScreen.route){
                popUpTo(0)
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(colorScheme.primary)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "QuickPark",
                    color = colorScheme.onPrimary,
                    fontFamily = poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
                Text(
                    text = "A Perfect spot for your Machine",
                    fontFamily = afacadflux,
                    color = colorScheme.onPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Create an account",
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            color = colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 70.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(value = name.value, onValueChange = { name.value = it }, label = {
            Text(
                text = "Your Name", fontFamily = poppins, color = colorScheme.onBackground
            )
        }, shape = RoundedCornerShape(20.dp), leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                tint = colorScheme.primary
            )
        })
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(value = email.value, onValueChange = { email.value = it }, label = {
            Text(
                text = "Email Address", fontFamily = poppins, color = colorScheme.onBackground
            )
        }, shape = RoundedCornerShape(20.dp), leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Mail,
                contentDescription = null,
                tint = colorScheme.primary
            )
        })
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(value = password.value,
            onValueChange = { password.value = it },
            label = {
                Text(
                    text = "Password", fontFamily = poppins, color = colorScheme.onBackground
                )
            },
            shape = RoundedCornerShape(20.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Lock,
                    contentDescription = null,
                    tint = colorScheme.primary
                )
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    imageVector = if (isPasswordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                    contentDescription = null,
                    tint = colorScheme.primary,
                    modifier = Modifier.clickable {
                        isPasswordVisible = !isPasswordVisible
                    }
                )
            }
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick = { viewModel.signUP(context, name.value, email.value, password.value)}, shape = RoundedCornerShape(20.dp), modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 70.dp)) {
            if (loading.value) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = "Sign Up",
                    fontFamily = poppins,
                    fontSize = 24.sp,
                    color = colorScheme.onPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        IconButton(onClick = { navController.popBackStack() },modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 70.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, colorScheme.primary, RoundedCornerShape(20.dp))) {
            Text(text = "Sign in", fontFamily = poppins, fontSize = 20.sp, color = colorScheme.onBackground)
        }
    }
}