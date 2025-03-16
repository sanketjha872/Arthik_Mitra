package com.jhainusa.arthik_.BottomNavPages


import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jhainusa.arthik_.BackendFiles.LoanViewModel
import com.jhainusa.arthik_.R
import com.jhainusa.arthik_.intern
import com.jhainusa.arthik_.uiComponents.SpeakerButton
import com.jhainusa.arthik_.uiComponents.inputBoxwithMic
import java.util.Date
import java.util.Locale


@Composable
fun budgethindi(viewModel: LoanViewModel = viewModel(),mainNav:NavController) {

    val totalAmount by viewModel.getTotalmoney().observeAsState(null)
    val totalEarn by viewModel.getTotalForCategogry("earned").observeAsState(null)
    val totalSpend by viewModel.getTotalForCategogry("spent").observeAsState(null)
    val totalDebt by viewModel.getTotalForCategogry("Debt").observeAsState(null)

    val safeTotalEarn = totalEarn ?: 0.0
    val safeTotalSpend = totalSpend ?: 0.0
    val safeTotalDebt = totalDebt ?: 0.0

    val context = LocalContext.current
    val tts = remember { TextToSpeech(context) { } }
    tts.language = Locale("hi", "IN")

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color(0xFFF1F0F0))
    ){
            upperBoxD(Color(0xFF723BE9))
            midparte(
                moneyspent = safeTotalDebt.toInt() + safeTotalSpend.toInt(),
                moneyearned = safeTotalEarn.toInt(),
                tts,
                context
            )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(rememberScrollState())
                .background(Color(0xFFF1F0F0))
        ){
            tagLine("खर्चा")
            val totals by viewModel.getTotalPerSubcategory("spent").observeAsState(emptyList())
            LazyColumn(
                modifier = Modifier.fillMaxWidth().height(250.dp)
            ) {
                items(totals) { total ->
                    sectionsa(
                        "spent",
                        total.subCategory,
                        painterResource(R.drawable.arcticons_ourgroceries),
                        Color(0xFFF4E2E2),
                        onTransactionAdded = { "jekl" },
                        navController = mainNav
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            tagLine("कमाई")

            val incomesections by viewModel.getTotalPerSubcategory("earned")
                .observeAsState(emptyList())
            LazyColumn(
                modifier = Modifier.fillMaxWidth().height(250.dp)

            ) {
                items(incomesections) { total ->
                    sectionsa(
                        "earned",
                        total.subCategory,
                        painterResource(R.drawable.arcticons_ourgroceries),
                        Color(0xFFEEE5EE),
                        onTransactionAdded = { "jekl" },
                        navController = mainNav
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }


    }

}

@Composable
fun upperBoxD(color: Color){
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().
        background(color)
            .padding(top = 45.dp, start = 15.dp, end = 15.dp)
    ){
        Image(
            painter = painterResource(R.drawable.whatsapp_image_2025_01_27_at_15_14_29_64d38f31),
            contentDescription = null
        )
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f).padding(start = 14.dp)
        ) {
            Text(
                text = "मेरा बजट",
                fontFamily = intern,
                fontSize = 19.sp,
                fontWeight = FontWeight.W600,
                color = Color.White,

            )
            Spacer(Modifier.height(1.dp))
            Text(
                text = "बजट" ,
                fontFamily = intern,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = Color.White

            )
        }
        txtbox()
    }
}

@Composable
fun midparte(
    moneyspent : Int,
    moneyearned : Int,
    tts: TextToSpeech,
    context: Context
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
            .background(Color(0xFF723BE9))
            .padding(vertical = 20.dp)

    ){
        boxmoneya(moneyearned,"अर्जित धन","कमाया गया",tts,context)
        boxmoneya(moneyspent,"खर्च की गई राशि","खर्च किया गया",tts,context)
    }

}
@Composable
fun boxmoneya(
    money: Int,
    type:String,
    hindi :String,
    tts: TextToSpeech,
    context: Context
){
    val animatedmoney by animateIntAsState(
        targetValue = money,
        animationSpec = tween(700)
    )
    val moneySpentHindi = convertNumberToHindiDigits(money).toInt()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "₹ $animatedmoney",
            fontSize = 32.sp,
            fontFamily = intern,
            fontWeight = FontWeight.W700,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = type,
            fontSize = 14.sp,
            fontFamily = intern,
            fontWeight = FontWeight.W500,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(3.dp))
        SpeakerButton(Color(0xFFFFD0A9),"$hindi $moneySpentHindi रुपये")
        Spacer(modifier = Modifier.height(7.dp))

    }

}


@Composable
private fun txtbox() {
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))

            .background(Color(0xFFA17DF0))

            .padding(horizontal = 5.dp, vertical = 2.dp)
    ) {
        Text(
            text = "${java.text.SimpleDateFormat("dd MMMM yyyy",Locale("hi", "IN")).format(Date())}",
            fontFamily = intern,
            fontSize = 13.sp,
            fontWeight = FontWeight.W500,
            color = Color.White
        )
    }
}

@Composable
fun sectionsa(
    type:String,
    itemName: String,
    itemImage : Painter,
    color: Color,
    viewModel: LoanViewModel = viewModel(),
    onTransactionAdded: () -> Unit,
    navController: NavController
)
{
    var iconClick by remember { mutableStateOf(false) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
            .padding(top = 8.dp , start = 8.dp , end = 8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color)


    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(7.dp,6.dp)

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {

                Image(
                    painter = itemImage,
                    contentDescription = null,
                    Modifier.size(35.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = itemName,
                    fontSize = 14.sp,
                    fontFamily = intern,
                    fontWeight = FontWeight.W500,
                    textAlign = TextAlign.Center

                )
            }
            Box(modifier = Modifier.weight(1.2f)) {
                SpeakerButton(sentence = "बताइए $itemName में आपने कितने खर्च किए")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val inp = remember { mutableStateOf("") }
                inputBoxwithMic(inp)
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(0.55f)
                ) {
                    IconButton(onClick = {
                        navController.navigate("screen5/$type/$itemName")
                    }
                    ) {
                        Icon(painter = painterResource(R.drawable.baseline_camera_alt_24), contentDescription = "null")

                    }
                    Button(
                        onClick = {
                            viewModel.addTransaction(
                                inp.value.toDoubleOrNull() ?: 0.0,
                                category = type,
                                subCategory = itemName
                            ); onTransactionAdded()
                            inp.value = ""
                        }
                    ) {
                        Text(
                            text = "राशि जोड़ें",
                            fontSize = 14.sp,
                            fontFamily = intern,
                            fontWeight = FontWeight.W500

                        )
                    }
                }
            }


        }
    }
}

fun convertNumberToHindiDigits(number: Int): String {
    val hindiDigits = mapOf(
        '0' to '०', '1' to '१', '2' to '२', '3' to '३', '4' to '४',
        '5' to '५', '6' to '६', '7' to '७', '8' to '८', '9' to '९'
    )
    return number.toString().map { hindiDigits[it] ?: it }.joinToString("")
}


