package com.jhainusa.arthik_

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Locale


val intern = FontFamily(
    Font(R.font.inter28ptmedium)
)

@Composable
fun loginScreen(navController: NavController){
   Column(
       modifier = Modifier.fillMaxSize()
           .verticalScroll(rememberScrollState())
           .imePadding()
           .background(Color(0xFFF1F0F0))
          ,
       verticalArrangement = Arrangement.Center,
       horizontalAlignment = Alignment.CenterHorizontally
   ) {
       Image(
       painter = painterResource(R.drawable.whatsapp_image_2025_01_27_at_15_14_29_64d38f31),
       contentDescription = null,
       modifier = androidx.compose.ui.Modifier
           .width(142.dp)
           .height(142.dp)
   )
       Spacer(modifier = Modifier.height(15.dp))
       Text(text="आर्थिक मित्र में आपका स्वागत है",
           fontSize = 27.sp,
           fontFamily = intern,
           fontWeight = FontWeight.W700
           )
       Spacer(modifier = Modifier.height(10.dp))
       txtcompos(text = "अपना मोबाइल नंबर और पासवर्ड दर्ज करके जारी रखें",
           Color(0xFF757575), fontWeight = FontWeight.W500)
       Spacer(modifier = Modifier.height(25.dp))
       input_screen("नाम", KeyboardType.Text,true)
       Spacer(modifier = Modifier.height(20.dp))
       input_screen("मोबाइल नंबर",KeyboardType.Number,false)
       Spacer(modifier = Modifier.height(20.dp))
       input_screen("पासवर्ड",KeyboardType.Password,false)
       Spacer(modifier = Modifier.height(10.dp))
       txtcompos(text = "पासवर्ड भूल गए",
           Color(0xFF633EB2),Arrangement.End, fontWeight = FontWeight.W600)

       Spacer(modifier = Modifier.height(20.dp))
       Button(
           onClick = { navController.navigate("screen4") },
           modifier = Modifier.fillMaxWidth(0.77f),
           shape = RoundedCornerShape(5.dp)
       ) {
           Text(text = "साइन इन",
               fontFamily = intern,
               fontWeight = FontWeight.W600,
               color = Color.White)
       }
   }
}

@Composable
private fun txtcompos(text: String,
                      color: Color,
                      horizontal: Arrangement.Horizontal = Arrangement.Center,
                      fontWeight: FontWeight
                      ) {
    Row(
        horizontalArrangement = horizontal,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(0.77f)
    ) {
        Text(
            text,
            color = color,
            fontSize = 15.sp,
            fontFamily = intern,
            fontWeight = fontWeight,
        )
    }
}

@Composable
fun input_screen(
    text : String,
    keyboardType: KeyboardType,
    isIcon:Boolean,
    isUpper : Boolean = true
){
    val context = LocalContext.current
    val tts = remember { TextToSpeech(context) { } }
    tts.language = Locale("hi", "IN")

    var iconClick by remember { mutableStateOf(false) }
    var inp by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isUpper) {
                Text(
                    text = text,
                    fontSize = 15.sp,
                    fontFamily = intern,
                    fontWeight = FontWeight.W600,
                )
                Spacer(Modifier.width(5.dp))
                Icon(
                    painter = painterResource(R.drawable.game_icons_speaker),
                    contentDescription = null,
                    Modifier.size(25.dp).clickable {
                        tts.language = Locale("hi", "IN")
                        tts.setSpeechRate(0.8f)
                        tts.speak("कृपया अपना $text लिखें" , TextToSpeech.QUEUE_FLUSH, null, null)

                    }
                )
            }
            Spacer(Modifier.height(5.dp))
        }

        TextField(
            value = inp,
            onValueChange = {
                inp = it
            },
            placeholder = {
                Text(
                    text = "$text दर्ज करें" ,
                    fontFamily = intern,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W500,
                    color = Color(0xFFA5A3A3)
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color(0xFFA5A3A3),

            ),
            shape = RoundedCornerShape(10.dp),
            trailingIcon = {
                if (isIcon) {
                    val context = LocalContext.current

                    val speechRecognizerLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartActivityForResult()
                    ) { result ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
                            spokenText?.let {
                                inp = it
                            }
                        }
                    }
                    IconButton(
                        onClick = {
                            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN") // Correct format for Hindi
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "hi-IN")
                                putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, true)
                                putExtra(RecognizerIntent.EXTRA_PROMPT, "अपना नाम बोलो")
                            }
                            speechRecognizerLauncher.launch(intent)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_mic_24),
                            contentDescription = null
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            modifier = Modifier.border(1.dp,Color(0xFFD9D9D9), shape = RoundedCornerShape(10.dp))

        )
    }
}