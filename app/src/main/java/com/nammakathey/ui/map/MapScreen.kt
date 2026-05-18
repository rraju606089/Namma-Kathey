package com.nammakathey.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nammakathey.utils.Constants
import com.nammakathey.viewmodel.HeroViewModel

private val DistrictColors = listOf(
    Color(0xFFFFCDD2), // Red 100
    Color(0xFFF8BBD0), // Pink 100
    Color(0xFFE1BEE7), // Purple 100
    Color(0xFFD1C4E9), // Deep Purple 100
    Color(0xFFC5CAE9), // Indigo 100
    Color(0xFFBBDEFB), // Blue 100
    Color(0xFFB3E5FC), // Light Blue 100
    Color(0xFFB2EBF2), // Cyan 100
    Color(0xFFB2DFDB), // Teal 100
    Color(0xFFC8E6C9)  // Green 100
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: HeroViewModel, onDistrictSelected: (String) -> Unit, onProfileClick: () -> Unit) {
    val lang by viewModel.language.collectAsState()
    val districts = viewModel.districts

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (lang == Constants.LANG_KANNADA) "ಜಿಲ್ಲೆಯನ್ನು ಆರಿಸಿ" else "Explore Districts",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFFFFDE7)
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text(
                text = if (lang == Constants.LANG_KANNADA) "ನಮ್ಮ ಕರ್ನಾಟಕ" else "Namma Karnataka",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFE65100),
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = if (lang == Constants.LANG_KANNADA) "ಒಂದು ಜಿಲ್ಲೆಯನ್ನು ಆಯ್ಕೆ ಮಾಡಿ" else "Tap on a district to learn about its heroes!",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(districts) { index, district ->
                    val color = DistrictColors[index % DistrictColors.size]
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .height(120.dp)
                            .clickable { onDistrictSelected(district.name) },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = color),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = district.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF424242)
                            )
                        }
                    }
                }
            }
        }
    }
}
