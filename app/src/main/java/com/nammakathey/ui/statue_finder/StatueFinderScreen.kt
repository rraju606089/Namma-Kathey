package com.nammakathey.ui.statue_finder

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.nammakathey.utils.Constants
import com.nammakathey.viewmodel.HeroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatueFinderScreen(viewModel: HeroViewModel, heroId: String, onBack: () -> Unit) {
    val lang by viewModel.language.collectAsState()
    val hero = viewModel.getHeroById(heroId) ?: return

    val heroLocation = LatLng(hero.statueLat, hero.statueLng)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(heroLocation, 12f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (lang == Constants.LANG_KANNADA) "ಸ್ಥಳ ಹುಡುಕಿ" else "Statue Finder") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        GoogleMap(
            modifier = Modifier.fillMaxSize().padding(padding),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = heroLocation),
                title = if (lang == Constants.LANG_KANNADA) hero.nameKn else hero.name,
                snippet = if (lang == Constants.LANG_KANNADA) "ಸ್ಮಾರಕ" else "Memorial"
            )
        }
    }
}
