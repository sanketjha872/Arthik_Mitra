package com.jhainusa.arthik_

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun TabSwitcher() {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Spendings, 1 = Earnings

    // Animated slider position
    val indicatorOffset by animateDpAsState(targetValue = if (selectedTab == 0) 0.dp else 100.dp, label = "")

    Box(
        modifier = Modifier
            .width(200.dp)
            .height(40.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(20.dp))
    ) {
        // Moving indicator
        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(100.dp)
                .height(40.dp)
                .background(Color.White, shape = RoundedCornerShape(20.dp))
        )

        // Tab options
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            TabItem("Spendings", selectedTab == 0) { selectedTab = 0 }
            TabItem("Earnings", selectedTab == 1) { selectedTab = 1 }
        }
    }
}

@Composable
fun TabItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
    }
}
