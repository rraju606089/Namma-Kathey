package com.nammakathey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nammakathey.ui.login.LoginScreen
import com.nammakathey.ui.hero_list.HeroListScreen
import com.nammakathey.ui.language.LanguageScreen
import com.nammakathey.ui.map.MapScreen
import com.nammakathey.ui.onboarding.OnboardingScreen
import com.nammakathey.ui.profile.ProfileScreen
import com.nammakathey.ui.quiz.QuizScreen
import com.nammakathey.ui.splash.SplashScreen
import com.nammakathey.ui.statue_finder.StatueFinderScreen
import com.nammakathey.ui.story.StoryScreen
import com.nammakathey.ui.theme.NammaKatheyTheme
import com.nammakathey.viewmodel.HeroViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NammaKatheyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier.weight(1f)) {
                            AppNavigation()
                        }
                        // Footer Branding
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shadowElevation = 4.dp
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Power By 413",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: HeroViewModel = viewModel()
    val onboardingCompleted by viewModel.isOnboardingCompleted.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onFinished = {
                if (isLoggedIn) {
                    navController.navigate("map") {
                        popUpTo("splash") { inclusive = true }
                    }
                } else if (onboardingCompleted) {
                    navController.navigate("language") {
                        popUpTo("splash") { inclusive = true }
                    }
                } else {
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            })
        }
        composable("onboarding") {
            OnboardingScreen(onFinished = {
                viewModel.completeOnboarding()
                navController.navigate("language") {
                    popUpTo("onboarding") { inclusive = true }
                }
            })
        }
        composable("language") {
            LanguageScreen(viewModel, onLanguageSelected = {
                navController.navigate("login")
            })
        }
        composable("login") {
            LoginScreen(viewModel, onLoginSuccess = {
                navController.navigate("map") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("map") {
            MapScreen(viewModel, onDistrictSelected = { districtName ->
                navController.navigate("hero_list/$districtName")
            }, onProfileClick = {
                navController.navigate("profile")
            })
        }
        composable(
            "hero_list/{districtName}",
            arguments = listOf(navArgument("districtName") { type = NavType.StringType })
        ) { backStackEntry ->
            val districtName = backStackEntry.arguments?.getString("districtName") ?: ""
            HeroListScreen(viewModel, districtName, onHeroClick = { heroId ->
                navController.navigate("story/$heroId")
            }, onBack = { navController.popBackStack() })
        }
        composable(
            "story/{heroId}",
            arguments = listOf(navArgument("heroId") { type = NavType.StringType })
        ) { backStackEntry ->
            val heroId = backStackEntry.arguments?.getString("heroId") ?: ""
            StoryScreen(viewModel, heroId, onQuizStart = {
                navController.navigate("quiz/$heroId")
            }, onBack = { navController.popBackStack() }, onStatueFinder = {
                navController.navigate("statue_finder/$heroId")
            })
        }
        composable(
            "quiz/{heroId}",
            arguments = listOf(navArgument("heroId") { type = NavType.StringType })
        ) { backStackEntry ->
            val heroId = backStackEntry.arguments?.getString("heroId") ?: ""
            QuizScreen(viewModel, heroId, onQuizComplete = {
                navController.popBackStack()
            })
        }
        composable("profile") {
            ProfileScreen(viewModel, onBack = { navController.popBackStack() })
        }
        composable(
            "statue_finder/{heroId}",
            arguments = listOf(navArgument("heroId") { type = NavType.StringType })
        ) { backStackEntry ->
            val heroId = backStackEntry.arguments?.getString("heroId") ?: ""
            StatueFinderScreen(viewModel, heroId, onBack = { navController.popBackStack() })
        }
    }
}
