package uk.ac.tees.mad.univid.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.univid.R
import uk.ac.tees.mad.univid.ui.theme.afacadflux
import uk.ac.tees.mad.univid.ui.theme.poppins

@Composable
fun SplashScreen(navController: NavHostController) {
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 3000,
                easing = EaseOutBounce
            )
        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.dall_e_2024_10_03_10_24_58___a_clean_and_modern_app_logo_for_a__quick_parking__app__the_design_should_feature_a_parking_symbol__p__prominently__combined_with_a_map_pin_icon_to_rep),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .scale(scale.value)
                .clip(RoundedCornerShape(20.dp))
                .shadow(20.dp)

        )
        Text(text = "QuickPark", fontFamily = poppins, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Text(text = "A Perfect spot for your Machine", fontFamily = afacadflux)
    }
}