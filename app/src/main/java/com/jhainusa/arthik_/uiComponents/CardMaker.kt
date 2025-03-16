package com.jhainusa.arthik_.uiComponents
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jhainusa.arthik_.BackendFiles.LoanViewModel
import com.jhainusa.arthik_.BottomNavPages.TransactionsScreen
import com.jhainusa.arthik_.BottomNavPages.convertNumberToHindiDigits
import com.jhainusa.arthik_.R
import com.jhainusa.arthik_.intern
import com.jhainusa.arthik_.ui.theme.AppleGray
import com.jhainusa.arthik_.ui.theme.purpleBack


@Preview
@Composable
fun CardMaker(viewModel: LoanViewModel = viewModel()) {
    Box(modifier = Modifier.background(Color(0xFFF1F0F0),).padding(8.dp),
        contentAlignment = Alignment.Center) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(25.dp),

                ) {
            val totals by viewModel.getTotalPerSubcategory("Debt").observeAsState(emptyList())
            LazyColumn {
                items(totals) { total ->
                    CardContent(money= total.totalAmount.toInt(),person = total.subCategory,
                        onTransactionAdded = {"jsv"}
                        )
                }
            }
        }
    }
}

// ui for card content
@Composable
fun CardContent(
    money: Int,
    person: String,
    viewModel: LoanViewModel = viewModel(),
    onTransactionAdded: () -> Unit
)
{
    val animatedMoney by animateIntAsState(
        targetValue = money,
        animationSpec = tween(durationMillis = 500) // Smooth animation
    )
    var showHistory by remember { mutableStateOf(false) }
    val moneySpentHindi = convertNumberToHindiDigits(money).toInt()
    var expanded by remember { mutableStateOf(false) }
    var isRotated by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (isRotated) 180f else 0f, // Toggle between 0° and 90°
        label = "rotation",
        animationSpec= spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            then(
                if(expanded) Modifier.padding(vertical = 7.dp)
                else Modifier
            )
    ) {
    Column (
        modifier = Modifier
            .then(
                if(expanded) Modifier.clip(RoundedCornerShape(25.dp)).border(1.dp,Color(0xFFE1DEDE),
                    RoundedCornerShape(25.dp))
                else Modifier
            )
            .background(Color.White)
            .padding(10.dp)
            .animateContentSize(
                animationSpec= spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .clickable {
                    expanded=!expanded
                    isRotated = !isRotated
                }
                .padding(horizontal = 12.dp, vertical = 8.dp)

        ) {
            Text(
                text = person ,
                fontFamily = FontFamily(Font(R.font.inter28ptmedium)),
                color = AppleGray,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)

            )
            Text(
                text = "₹$animatedMoney",
                fontFamily = FontFamily(Font(R.font.inter28ptmedium)),
                color = Color(0xFF388E3C), // Green color for balance
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                isRotated = !isRotated
                expanded = !expanded
            }) {
                Icon(
                    painter = painterResource(R.drawable.downarrow),
                    contentDescription =
                    if (expanded) {
                        "show less"
                    } else {
                        "show more"
                    },
                    tint = Color(0xFFAEAEB2),

                    modifier = Modifier.rotate(rotationAngle).size(22.dp)
                )
            }
        }
        if (expanded) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                val inp = remember { mutableStateOf("") }
                inputBoxwithMic(inp)
                Button(
                    onClick = {
                        if(inp.value!="0" && inp.value!="") {
                            viewModel.addTransaction(
                                inp.value.toDoubleOrNull() ?: 0.0,
                                "Debt",
                                subCategory = person,
                            ); onTransactionAdded()
                            inp.value = ""
                        }
                    },
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.White,
                        disabledContentColor = Color.White,
                        disabledContainerColor = Color.White
                    ),
                    border = BorderStroke(0.5.dp,Color.Gray),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        1.dp,0.5.dp,
                    )
                ) {
                    Text(
                        text = "राशि जोड़ें",
                        fontFamily = intern,
                        color = Color(0xFF723BE9)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            // History Section Title
            Button(
                onClick = {
                    showHistory = !showHistory
                },
                colors = ButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.White,
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color.White ),
                border = BorderStroke(0.5.dp, Color.LightGray),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    1.dp,0.5.dp,
                ),
                shape = RoundedCornerShape(15.dp)

            ) {
                Text(
                    text = "इतिहास",
                    fontSize = 15.sp,
                    fontFamily = intern,
                    color = purpleBack,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(bottom = 3.dp, start = 5.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            if(showHistory) {
                TransactionsScreen("Debt", subCategory = person, viewModel)
            }

        }


    }
    }
}