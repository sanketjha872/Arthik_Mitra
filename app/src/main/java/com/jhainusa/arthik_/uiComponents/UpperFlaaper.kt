package com.jhainusa.arthik_.uiComponents

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhainusa.arthik_.intern

@Composable
fun UpperFlapper(name:String,
                 content : @Composable () -> Unit ={}
                 ){
    var startAnimation by remember { mutableStateOf(false) }

    val offsetY by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else -100.dp, // Moves from -200dp to 0dp
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing) // Smooth bounce effect
    )
    LaunchedEffect(
        Unit
    ) {
        startAnimation=true
    }
    Card(

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(
            bottomStart = 10.dp, bottomEnd = 10.dp
        ),
        modifier = Modifier.
        offset(y=offsetY)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(horizontal = 18.dp).fillMaxWidth().padding(top = 45.dp, bottom = 11.dp),
        ) {
            Icon(
                Icons.Default.KeyboardArrowLeft, contentDescription = null,
                modifier = Modifier.size(35.dp)
            )
            Text(
                text = name, fontSize = 20.sp,
                fontFamily = intern,
                color = Color(0xFF051E71),
                modifier = Modifier.weight(1f)
            )
            content()
        }
    }
}