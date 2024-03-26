package com.example.chooseu.core.diary.cache

import com.example.chooseu.core.diary.models.FoodItem
import javax.inject.Singleton

@Singleton
class FoodItemCache {
    val map = mutableMapOf<String, FoodItem>()
}