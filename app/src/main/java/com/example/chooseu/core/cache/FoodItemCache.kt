package com.example.chooseu.core.cache

import com.example.chooseu.ui.screens.nutrition_screen.FoodItem
import javax.inject.Singleton

@Singleton
class FoodItemCache {
    val map = mutableMapOf<String, FoodItem>()
}