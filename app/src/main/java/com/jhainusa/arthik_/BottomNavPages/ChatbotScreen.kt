package com.jhainusa.arthik_.BottomNavPages

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.ai.client.generativeai.GenerativeModel
import com.jhainusa.arthik_.R
import com.jhainusa.arthik_.intern
import com.jhainusa.arthik_.uiComponents.UpperFlapper
import kotlinx.coroutines.launch
import java.util.Locale

@Preview
@Composable
fun ChatBotScreen() {
    var isLoading by remember { mutableStateOf(false) }

    val apiKey = "AIzaSyCjLHvAfyn_TmcV9oVxAePdJOesrkFtcLQ" // 🔹 Replace with your Gemini API key
    var userInput by remember { mutableStateOf(TextFieldValue("")) }
    val messages = remember { mutableStateListOf<Pair<String, Boolean>>() } // (message, isUser)
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            spokenText?.let {
                userInput = TextFieldValue(it)
            }
        }
    }
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN") // Correct format for Hindi
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "hi-IN")
        putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, true)
        putExtra(RecognizerIntent.EXTRA_PROMPT, "कुछ भी पूछें...")
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true) // Helps improve accuracy
    }
    val model = GenerativeModel(modelName = "gemini-1.5-flash", apiKey = apiKey)

    fun sendMessage() {
        if (userInput.text.isNotBlank()) {
            val inputText = userInput.text
            messages.add(inputText to true) // User message
            userInput = TextFieldValue("")

            // Fetch Gemini's response in Hindi
            coroutineScope.launch {
                val response = try {
                    val result = model.generateContent(
                        "तुम एक साधारण हिंदी में बात करने वाले हो। आसान भाषा में उत्तर दो: $inputText" +
                                "\nसीधे और सरल शब्दों में जवाब दो, बिना किसी बुलेट पॉइंट या चिन्ह के।"
                    )
                    result.text ?: "मुझे समझ नहीं आया।"
                } catch (e: Exception) {
                    "मुझे माफ करें, कुछ गलत हो गया।"
                }
                isLoading = false
                messages.add(response to false) // Bot response
            }
        }
    }


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(Color(0xFFF1F0F0))
    ) {
        UpperFlapper("हिसाबAI")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp, horizontal = 7.dp)
                .background(Color(0xFFF1F0F0))
                .clip(RoundedCornerShape(20.dp))
                .shadow(15.dp)
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "नमस्ते उपयोगकर्ता!",
                fontSize = 19.sp,
                fontFamily = intern,
                color = Color(0xFF6A0DAD),
                fontWeight = FontWeight.W600
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "मैं आपकी कैसे मदद कर सकता हूँ?",
                fontSize = 18.sp,
                fontFamily = intern,
                color = Color(0xFF6A0DAD)
            )

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier
                    .height(360.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 19.dp, vertical = 10.dp)
                    .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
                    .padding(4.dp)
            ) {
                item {
                    messages.forEach { (message, isUser) ->
                        ChatBubble(message, isUser)
                    }
                }
            }


            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    placeholder = { Text("कुछ भी पूछें...") },
                    modifier = Modifier
                        .weight(1f),
                    trailingIcon = {
                        Row() {
                            IconButton(
                                onClick = {
                                    speechRecognizerLauncher.launch(intent)
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_mic_24),
                                    contentDescription = null
                                )
                            }
                            IconButton(onClick = {
                                isLoading = true
                                sendMessage()
                            }) {
                                if (isLoading)
                                    CircularProgressIndicator(
                                        color = Color.Gray,
                                        modifier = Modifier.size(25.dp)
                                    )
                                else
                                    Icon(
                                        Icons.Default.Send,
                                        contentDescription = "Send"
                                    )
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Gray,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: String, isUser: Boolean) {
    var clicked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val textToSpeech = remember { TextToSpeech(context) { } }
    textToSpeech.language = Locale("hi", "IN")
    textToSpeech.setSpeechRate(0.8f)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                clicked = !clicked
            },
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Text(
            text = message,
            fontSize = 16.sp,
            color = Color.White,
            fontFamily = intern,
            modifier = Modifier
                .background(
                    if (!isUser) Color(0xFF597EBB) else Color.Gray,
                    RoundedCornerShape(10.dp)
                )
                .padding(10.dp)

        )
        if (clicked) {
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
        }
        if (!clicked) {
            textToSpeech.stop()
        }

    }
}
