package com.jhainusa.arthik_.BottomNavPages

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jhainusa.arthik_.BackendFiles.LoanViewModel
import com.jhainusa.arthik_.R
import com.jhainusa.arthik_.intern
import com.jhainusa.arthik_.ui.theme.AppleGray
import com.jhainusa.arthik_.ui.theme.Green
import com.jhainusa.arthik_.uiComponents.UpperFlapper
import kotlin.random.Random


@Preview
@Composable
fun ExpenseTrackerScreen() {
    var selectedTab by remember { mutableStateOf("Spending") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F0F0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UpperFlapper("प्रवृत्तियाँ")
        val colors = remember { generateRandomColors(100) }
        ExpenseTabLayout(viewModel(),colors)
    }
}

@Composable
fun boxIcon(txt:String){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .shadow(0.5.dp, RoundedCornerShape(5.dp))
            .background(Color.White, RoundedCornerShape(5.dp))
            .border(1.dp, color = Color.LightGray,RoundedCornerShape(7.dp))
            .padding(start = 11.dp, end = 6.dp, top = 9.dp, bottom = 9.dp)

    ){
        Text(txt,
            fontSize = 15.sp,
            fontFamily = intern,
            color = Color.DarkGray)
        Icon(
            painter = painterResource(R.drawable.baseline_keyboard_arrow_down_24),
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpenseTabLayout(
    viewModel: LoanViewModel = viewModel(),
    colors: List<Color>
) {

    val tabTitles = listOf("खर्च", " आय")
    var selectedIndex by remember { mutableStateOf(0) }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(vertical = 8.dp, horizontal = 10.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Color.White)
            .padding(15.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedIndex,
            containerColor = Color.Transparent,
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index },
                    text = {
                        Text(
                            title,
                            fontFamily = intern,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.W600,
                            color = if (selectedIndex == index) Color.Black else Color.Gray
                        )
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(45.dp))


            when (selectedIndex) {

                0 -> earnings("spent", colors)  // Show Spending Screen
                1 -> earnings("earned", colors)  // Show Earnings Screen
            }

    }
}


// Pie Chart Composable
@Composable
fun PieChart(
    viewModel: LoanViewModel = viewModel(),
    colors: List<Color>,
    type: String
) {
    val totals by viewModel.getTotalPerSubcategory(type).observeAsState(emptyList())
    val totalAmount by viewModel.getTotalForCategogry(type).observeAsState(0.0)

    val animationProgress = remember { Animatable(0f) }

    // Start the animation once the totals are loaded
    LaunchedEffect(totals) {
        animationProgress.animateTo(1f, animationSpec = tween(600, easing = FastOutSlowInEasing))
    }


    Canvas(modifier = Modifier.size(150.dp)) {
        var startAngle = 0f
        totals.forEachIndexed { index, value ->
            val sweepAngle = (value.totalAmount / totalAmount.toFloat()).toFloat() * 360f * animationProgress.value

            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 20.dp.toPx()),
                size = Size(size.width, size.height),
                topLeft = Offset(0f, 0f)
            )
            startAngle += sweepAngle
        }
    }
}


@Composable
fun earnings(
    type : String,
    colors: List<Color>,
    viewModel: LoanViewModel = viewModel()
)
{
    val totalAmount by viewModel.getTotalForCategogry(type).observeAsState(0.0)
    val totals by viewModel.getTotalPerSubcategory(type).observeAsState(emptyList())
    val animatedValue = remember { Animatable(0f) }

    // Start the animation when the targetValue changes
    LaunchedEffect(totalAmount) {
        animatedValue.animateTo(
            targetValue = totalAmount!!.toFloat(),
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        )
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(1f)
        ){

        Box(
            contentAlignment = Alignment.Center
        ){

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                    Text(
                        text = "कुल रकम", fontSize = 18.sp,
                        fontFamily = intern,
                        color = AppleGray,
                        modifier = Modifier.padding(top = 7.dp)
                    )
                Text(
                    text = "₹"+"%.0f".format(animatedValue.value), fontSize = 20.sp,
                    fontFamily = intern,
                    color = Green,
                    modifier = Modifier.padding(top=2.dp)

                )
            }

            PieChart(
                viewModel,colors, type
            )
        }

    Spacer(modifier = Modifier.height(60.dp))

    Text(text = "सभी श्रेणियाँ", fontSize = 18.sp,
        fontFamily = intern,
        color = Color.DarkGray,
        modifier = Modifier.padding(horizontal = 6.dp).fillMaxWidth())

    Spacer(modifier = Modifier.height(12.dp))
        var i : Int = 0
        LazyColumn { items(totals) { total ->
                    ExpenseCategoryRow(category = total.subCategory,
                        subamount =total.totalAmount,
                        percentage = (((total.totalAmount*100)/totalAmount.toInt()).toInt()),
                        color = colors[i++]
                    )
            Box(
                modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth().height(0.5.dp).background(Color.LightGray)
                    .clip(RoundedCornerShape(2.dp))
            )
        } }
}

}

@Composable
fun ExpenseCategoryRow(category: String, subamount : Double, percentage: Int, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 16.dp)) {
        Text(text = "$category", fontSize = 17.sp,
            fontFamily = intern,
            fontWeight = FontWeight.W600, color = Color(0xD92C2C2E),
            modifier = Modifier.padding(end = 10.dp).weight(1f)
        )
        Text(text = "₹$subamount", fontSize = 17.sp,
            fontFamily = intern,
            fontWeight = FontWeight.W600, color = Color(0xD92C2C2E),
            modifier = Modifier.weight(1f)
        )
        Text(text = "$percentage%", fontSize = 15.sp,
            fontFamily = intern,
            fontWeight = FontWeight.W800, color = Color.White,
            modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(color)
                .padding(horizontal = 13.dp, vertical = 5.dp)
        )
    }

}

fun generateRandomColors(size: Int): List<Color> {
    return List(size) {
        Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat(),
        )
    }
}
