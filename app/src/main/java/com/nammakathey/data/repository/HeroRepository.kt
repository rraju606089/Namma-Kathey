package com.nammakathey.data.repository

import android.content.Context
import com.google.gson.Gson
import com.nammakathey.data.local.BadgeDao
import com.nammakathey.data.model.AppData
import com.nammakathey.data.model.Badge
import com.nammakathey.data.model.District
import com.nammakathey.data.model.Hero
import kotlinx.coroutines.flow.Flow

class HeroRepository(private val context: Context, private val badgeDao: BadgeDao) {

    private var appData: AppData? = null

    fun getAppData(): AppData {
        if (appData == null) {
            val jsonString = context.assets.open("data.json").bufferedReader().use { it.readText() }
            appData = Gson().fromJson(jsonString, AppData::class.java)
        }
        return appData!!
    }

    fun getDistricts(): List<District> = getAppData().districts

    fun getHeroesByDistrict(districtName: String): List<Hero> {
        return getDistricts().find { it.name == districtName }?.heroes ?: emptyList()
    }

    fun getHeroById(heroId: String): Hero? {
        return getDistricts().flatMap { it.heroes }.find { it.id == heroId }
    }

    fun getAllBadges(): Flow<List<Badge>> = badgeDao.getAllBadges()

    suspend fun earnBadge(heroId: String, heroName: String) {
        if (!badgeDao.hasBadge(heroId)) {
            badgeDao.insertBadge(Badge(heroId, heroName, System.currentTimeMillis()))
        }
    }
}
