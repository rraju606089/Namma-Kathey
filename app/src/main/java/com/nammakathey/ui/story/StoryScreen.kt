package com.nammakathey.ui.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nammakathey.utils.Constants
import com.nammakathey.utils.TTSManager
import com.nammakathey.viewmodel.HeroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryScreen(viewModel: HeroViewModel, heroId: String, onQuizStart: () -> Unit, onBack: () -> Unit, onStatueFinder: () -> Unit) {
    val lang by viewModel.language.collectAsState()
    val hero = viewModel.getHeroById(heroId) ?: return
    val context = LocalContext.current
    val ttsManager = remember { TTSManager(context) }

    val pagerState = rememberPagerState(pageCount = { hero.stories.size })

    DisposableEffect(Unit) {
        onDispose {
            ttsManager.shutdown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (lang == Constants.LANG_KANNADA) hero.nameKn else hero.name, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onStatueFinder) {
                        Icon(Icons.Default.Place, contentDescription = "Statue Finder", tint = Color.Red)
                    }
                    IconButton(onClick = {
                        val currentStory = hero.stories[pagerState.currentPage]
                        val textToSpeak = if (lang == Constants.LANG_KANNADA) currentStory.contentKn else currentStory.content
                        ttsManager.speak(textToSpeak, lang)
                    }) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Read Aloud", tint = Color(0xFF4CAF50))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF3E5F5)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
            ) { page ->
                val storyPage = hero.stories[page]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (lang == Constants.LANG_KANNADA) storyPage.titleKn else storyPage.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF7B1FA2),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    val imageRes = context.resources.getIdentifier(storyPage.image.trim(), "drawable", context.packageName)
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF5F5F5)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageRes != 0) {
                            AsyncImage(
                                model = imageRes,
                                contentDescription = storyPage.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Icon(
                                Icons.Default.MenuBook,
                                contentDescription = null,
                                modifier = Modifier.size(120.dp),
                                tint = Color(0xFF9C27B0).copy(alpha = 0.3f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = if (lang == Constants.LANG_KANNADA) storyPage.contentKn else storyPage.content,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )
                }
            }

            // Bottom controls
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (pagerState.currentPage == hero.stories.size - 1) {
                    Button(
                        onClick = onQuizStart,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
                    ) {
                        Text(
                            if (lang == Constants.LANG_KANNADA) "ರಸಪ್ರಶ್ನೆ ಪ್ರಾರಂಭಿಸಿ!" else "Start the Quiz!",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                } else {
                    // Page Indicator
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(hero.stories.size) { iteration ->
                            val color = if (pagerState.currentPage == iteration) Color(0xFF9C27B0) else Color.LightGray
                            Surface(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(12.dp),
                                shape = androidx.compose.foundation.shape.CircleShape,
                                color = color
                            ) {}
                        }
                    }
                }
            }
        }
    }
}
