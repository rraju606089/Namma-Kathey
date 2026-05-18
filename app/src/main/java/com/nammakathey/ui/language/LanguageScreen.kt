package com.nammakathey.ui.language

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammakathey.utils.Constants
import com.nammakathey.viewmodel.HeroViewModel

@Composable
fun LanguageScreen(viewModel: HeroViewModel, onLanguageSelected: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFE0B2), Color(0xFFFF9800))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Namaskara! \n ನಮಸ್ಕಾರ!",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "Choose Your Language \n ಭಾಷೆಯನ್ನು ಆರಿಸಿ",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            LanguageButton(
                text = "English",
                onClick = {
                    viewModel.setLanguage(Constants.LANG_ENGLISH)
                    onLanguageSelected()
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LanguageButton(
                text = "ಕನ್ನಡ (Kannada)",
                onClick = {
                    viewModel.setLanguage(Constants.LANG_KANNADA)
                    onLanguageSelected()
                }
            )
        }
    }
}

@Composable
fun LanguageButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        shape = RoundedCornerShape(35.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color(0xFFE65100)
        ),
        elevation = ButtonDefaults.buttonElevation(8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
