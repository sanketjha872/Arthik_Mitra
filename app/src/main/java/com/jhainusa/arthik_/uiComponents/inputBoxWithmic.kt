package com.jhainusa.arthik_.uiComponents

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhainusa.arthik_.R
import com.jhainusa.arthik_.intern

@Composable
fun inputBoxwithMic(inp: MutableState<String>){
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            spokenText?.let {
                inp.value = it.filter { it.isDigit() } // Extract only numbers
            }
        }
    }
    TextField(
        value = inp.value,
        onValueChange = {
            inp.value = it
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF1F0F0),
            unfocusedContainerColor = Color(0xFFF1F0F0),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,


            ),
        shape = RoundedCornerShape(10.dp),
        textStyle = LocalTextStyle.current.copy(
            fontSize = 17.sp,
            fontFamily = intern,
            fontWeight = FontWeight.ExtraBold
        ),

        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.mdi_rupee),
                contentDescription = null,
                tint = Color.DarkGray,
                modifier = Modifier.size(22.dp)

            )
        },
        trailingIcon = {

            IconButton(
                onClick = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN") // Correct format for Hindi
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "hi-IN")
                        putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, true)// Hindi Input
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "बोलकर राशि दर्ज करें")
                    }
                    speechRecognizerLauncher.launch(intent)
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_mic_24),
                    contentDescription = null
                )
            }

        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.fillMaxWidth(0.6f).height(50.dp)
            .shadow(1.dp, RoundedCornerShape(10.dp))
            .border(1.dp, Color(0xFFD9D9D9), shape = RoundedCornerShape(10.dp))


    )
}