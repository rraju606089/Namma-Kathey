package com.nammakathey.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class AppData(
    val districts: List<District>
)

data class District(
    val name: String,
    val heroes: List<Hero>
)

data class Hero(
    val id: String,
    val name: String,
    val nameKn: String,
    val image: String,
    val shortDescription: String,
    val shortDescriptionKn: String,
    val stories: List<StoryPage>,
    val quiz: List<QuizQuestion>,
    val statueLat: Double,
    val statueLng: Double
)

data class StoryPage(
    val title: String,
    val titleKn: String,
    val image: String,
    val content: String,
    val contentKn: String
)

data class QuizQuestion(
    val question: String,
    val questionKn: String,
    val options: List<String>,
    val optionsKn: List<String>,
    val correctAnswerIndex: Int
)

@Entity(tableName = "badges")
data class Badge(
    @PrimaryKey val heroId: String,
    val heroName: String,
    val earnedDate: Long
)
