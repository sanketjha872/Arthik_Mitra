package com.jhainusa.arthik_.uiComponents

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jhainusa.arthik_.R
import java.util.Locale

@Composable
fun SpeakerButton(color: Color = Color.Black,sentence:String){
    val context = LocalContext.current
    val tts = remember { TextToSpeech(context) { } }
    tts.language = Locale("hi", "IN")
    Icon(painter = painterResource(R.drawable.game_icons_speaker),
        contentDescription = null,
        Modifier.size(35.dp)
            .clickable {
                tts.language = Locale("hi", "IN")
                tts.setSpeechRate(0.75f)
                tts.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, null)
            } ,
        tint = color,
    )
}