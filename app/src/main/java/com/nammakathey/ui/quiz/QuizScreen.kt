package com.nammakathey.ui.quiz

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammakathey.utils.Constants
import com.nammakathey.viewmodel.HeroViewModel
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(viewModel: HeroViewModel, heroId: String, onQuizComplete: () -> Unit) {
    val lang by viewModel.language.collectAsState()
    val hero = viewModel.getHeroById(heroId) ?: return
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }

    val currentQuestion = hero.quiz[currentQuestionIndex]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE1F5FE))
    ) {
        if (showResult) {
            ResultView(
                score = score,
                total = hero.quiz.size,
                lang = lang,
                onFinish = {
                    if (score == hero.quiz.size) {
                        viewModel.earnBadge(hero.id, hero.name)
                    }
                    onQuizComplete()
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = (currentQuestionIndex + 1).toFloat() / hero.quiz.size,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = Color(0xFF03A9F4),
                    trackColor = Color.White
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = if (lang == Constants.LANG_KANNADA) "ಪ್ರಶ್ನೆ ${currentQuestionIndex + 1}" else "Question ${currentQuestionIndex + 1}",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF01579B)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Text(
                        text = if (lang == Constants.LANG_KANNADA) currentQuestion.questionKn else currentQuestion.question,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(24.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                currentQuestion.options.forEachIndexed { index, option ->
                    val displayOption = if (lang == Constants.LANG_KANNADA) currentQuestion.optionsKn[index] else option
                    val backgroundColor = when {
                        selectedOptionIndex == index && isCorrect == true -> Color(0xFF81C784)
                        selectedOptionIndex == index && isCorrect == false -> Color(0xFFE57373)
                        else -> Color.White
                    }
                    
                    Button(
                        onClick = {
                            if (selectedOptionIndex == null) {
                                selectedOptionIndex = index
                                isCorrect = index == currentQuestion.correctAnswerIndex
                                if (isCorrect!!) score++
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(60.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = backgroundColor,
                            contentColor = if (selectedOptionIndex == index) Color.White else Color(0xFF0277BD)
                        ),
                        elevation = ButtonDefaults.buttonElevation(4.dp),
                        enabled = selectedOptionIndex == null
                    ) {
                        Text(displayOption, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                if (selectedOptionIndex != null) {
                    Button(
                        onClick = {
                            if (currentQuestionIndex < hero.quiz.size - 1) {
                                currentQuestionIndex++
                                selectedOptionIndex = null
                                isCorrect = null
                            } else {
                                showResult = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                    ) {
                        Text(
                            if (lang == Constants.LANG_KANNADA) "ಮುಂದೆ" else "Next",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResultView(score: Int, total: Int, lang: String, onFinish: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = Color(0xFFFFD600)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = if (lang == Constants.LANG_KANNADA) "ಅದ್ಭುತ!" else "Well Done!",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF0277BD),
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (lang == Constants.LANG_KANNADA) "ನಿಮ್ಮ ಸ್ಕೋರ್: $score/$total" else "Your Score: $score/$total",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        if (score == total) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("🏅", fontSize = 40.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        if (lang == Constants.LANG_KANNADA) "ನೀವು ಬ್ಯಾಡ್ಜ್ ಗೆದ್ದಿದ್ದೀರಿ!" else "You won a Badge!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onFinish,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text(if (lang == Constants.LANG_KANNADA) "ಮುಕ್ತಾಯ" else "Finish", style = MaterialTheme.typography.titleLarge)
        }
    }
}
