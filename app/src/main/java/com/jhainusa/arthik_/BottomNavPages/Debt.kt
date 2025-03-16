package com.jhainusa.arthik_.BottomNavPages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jhainusa.arthik_.BackendFiles.LoanViewModel
import com.jhainusa.arthik_.intern
import com.jhainusa.arthik_.ui.theme.Green
import com.jhainusa.arthik_.uiComponents.CardMaker
import com.jhainusa.arthik_.uiComponents.CategoryScreen
import com.jhainusa.arthik_.uiComponents.UpperFlapper
import com.jhainusa.arthik_.uiComponents.inputBoxwithMic
import java.util.Locale

@Preview
@Composable
fun DebtManagementScreen(viewModel: LoanViewModel = viewModel()) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F0F0),)
    ) {
        UpperFlapper("कर्ज", content = @androidx.compose.runtime.Composable { CategoryScreen(viewModel(),"Debt", onTransactionAdded = {"kn"}) })
        CardMaker()
    }
}

@Composable
fun tagLine(type: String){
    val nameToSurname = mutableMapOf(
         "खर्चा" to "spent",
        "कमाई" to "earned",
        "कर्ज" to "debt"
    )
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth().padding(vertical = 13.dp)
    ) {
        Text(
            text = type,
            fontSize = 19.sp,
            fontFamily = intern,
            color = Color(0xFF051E71),
            fontWeight = FontWeight.W500,
        )
        CategoryScreen(viewModel(), nameToSurname[type].toString(), onTransactionAdded = {"kn"})
    }
}


@Composable
fun AddDebtScreen(viewModel: LoanViewModel = viewModel(), onTransactionAdded: () -> Unit) {
    var amount = remember { mutableStateOf("") }
    var person = remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(16.dp)) {
        inputBoxwithMic(amount)
        inputBoxwithMic(person)
        Button(onClick = { viewModel.addTransaction(amount.value.toDoubleOrNull() ?: 0.0,"Debt", person.value,); onTransactionAdded() ;
            amount.value=""
            person.value=""
        }) {
            Text("Add Transaction")
        }
    }
}

@Composable
fun TransactionsScreen(category: String, subCategory: String, viewModel: LoanViewModel = viewModel()) {
    val transactions by viewModel.getTransactions(category, subCategory).observeAsState(emptyList())

    LazyColumn(
           modifier = Modifier.height(150.dp).fillMaxWidth().
               clip(RoundedCornerShape(15.dp)).
           background(Color(0xFFF1F0F0))
                .border(
                    0.5.dp, color = Color.LightGray, RoundedCornerShape(15.dp)
               )
       ) {
            items(transactions) { transaction ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(3.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xCCF1F0F0))
                            .padding(15.dp)

                        ,

                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "${java.text.SimpleDateFormat("dd MMMM yyyy \nhh:mm a",Locale("hi", "IN")).format(transaction.date)}", fontSize = 14.sp, fontFamily = intern, fontWeight = FontWeight.W500)
                        Text(text = "₹${transaction.amount}",color = Green, fontSize = 16.sp, fontFamily = intern ,fontWeight = FontWeight.Bold)
                    }
                }

        }
}
