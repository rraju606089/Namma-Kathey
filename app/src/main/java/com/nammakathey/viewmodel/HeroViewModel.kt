package com.nammakathey.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nammakathey.data.local.AppDatabase
import com.nammakathey.data.model.Badge
import com.nammakathey.data.model.District
import com.nammakathey.data.model.Hero
import com.nammakathey.data.repository.HeroRepository
import com.nammakathey.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HeroViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HeroRepository

    val language = MutableStateFlow(Constants.LANG_ENGLISH)
    val districts: List<District>
    val badges: StateFlow<List<Badge>>
    val isOnboardingCompleted = MutableStateFlow(false)
    val isLoggedIn = MutableStateFlow(false)
    val userName = MutableStateFlow("")

    init {
        val database = AppDatabase.getDatabase(application)
        repository = HeroRepository(application, database.badgeDao())
        districts = repository.getDistricts()
        badges = repository.getAllBadges().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        
        val prefs = application.getSharedPreferences(Constants.PREFS_NAME, Application.MODE_PRIVATE)
        language.value = prefs.getString(Constants.KEY_LANGUAGE, Constants.LANG_ENGLISH) ?: Constants.LANG_ENGLISH
        isOnboardingCompleted.value = prefs.getBoolean(Constants.KEY_ONBOARDING_COMPLETED, false)
        isLoggedIn.value = prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false)
        userName.value = prefs.getString(Constants.KEY_USER_NAME, "") ?: ""
    }

    fun login(name: String) {
        val prefs = getApplication<Application>().getSharedPreferences(Constants.PREFS_NAME, Application.MODE_PRIVATE)
        prefs.edit().apply {
            putBoolean(Constants.KEY_IS_LOGGED_IN, true)
            putString(Constants.KEY_USER_NAME, name)
            apply()
        }
        isLoggedIn.value = true
        userName.value = name
    }

    fun logout() {
        val prefs = getApplication<Application>().getSharedPreferences(Constants.PREFS_NAME, Application.MODE_PRIVATE)
        prefs.edit().apply {
            putBoolean(Constants.KEY_IS_LOGGED_IN, false)
            putString(Constants.KEY_USER_NAME, "")
            apply()
        }
        isLoggedIn.value = false
        userName.value = ""
    }

    fun setLanguage(lang: String) {
        language.value = lang
        val prefs = getApplication<Application>().getSharedPreferences(Constants.PREFS_NAME, Application.MODE_PRIVATE)
        prefs.edit().putString(Constants.KEY_LANGUAGE, lang).apply()
    }

    fun completeOnboarding() {
        isOnboardingCompleted.value = true
        val prefs = getApplication<Application>().getSharedPreferences(Constants.PREFS_NAME, Application.MODE_PRIVATE)
        prefs.edit().putBoolean(Constants.KEY_ONBOARDING_COMPLETED, true).apply()
    }

    fun getHeroesForDistrict(districtName: String): List<Hero> {
        return repository.getHeroesByDistrict(districtName)
    }

    fun getHeroById(heroId: String): Hero? {
        return repository.getHeroById(heroId)
    }

    fun earnBadge(heroId: String, heroName: String) {
        viewModelScope.launch {
            repository.earnBadge(heroId, heroName)
        }
    }
}
