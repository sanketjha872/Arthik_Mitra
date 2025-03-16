package com.jhainusa.arthik_

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

var font = FontFamily(
    Font(R.font.kavoon),
    Font(R.font.intern1)
)


@Composable
fun screen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFF723BE9)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
       Image(
           painter = painterResource(R.drawable.arthik_mitralogo),
           contentDescription = null,
           modifier = Modifier.padding(top=15.dp,bottom = 50.dp)
               .width(142.dp)
               .height(142.dp)
       )
        txt("Arthik",Alignment.TopStart)
        txt("Mitra",Alignment.TopEnd)

        Text(text = "Every rupee counts",
            color = Color.White,
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.kalam_reg)),
            modifier = Modifier.padding(end = 50.dp, bottom = 40.dp,top=10.dp))

        Button(
            onClick = {
                navController.navigate("screen3")
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF3300A4)),
            modifier = Modifier.padding(top = 50.dp)
        ) {
            Text(text = "Get Started",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.intern1)),
            modifier = Modifier.padding(1.dp))

        }


    }
}

@Composable
fun txt(txt:String,contentAlignment: Alignment){
    Box(
        contentAlignment = contentAlignment,
        modifier = Modifier.fillMaxWidth(0.7f)
    ) {
        Text(
            text = txt,
            fontSize = 60.sp,
            color = Color.White,
            fontFamily = font
        )
    }
}