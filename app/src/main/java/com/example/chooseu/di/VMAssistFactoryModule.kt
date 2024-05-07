package com.example.chooseu.di

import com.example.chooseu.core.MealType
import com.example.chooseu.ui.screens.main_screen.BottomNavViewModel
import com.example.chooseu.ui.screens.profile.ProfileViewModel
import com.example.chooseu.ui.screens.calendar_access.CalendarSettingViewModel
import com.example.chooseu.ui.screens.food_search.FoodSearchViewModel
import com.example.chooseu.ui.screens.home.HomeViewModel
import com.example.chooseu.ui.screens.nutrition_screen.NutritionViewModel
import dagger.Module
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class VMAssistFactoryModule {
    @AssistedFactory
    interface BottomNavVmFactory {
        fun create(userId: String): BottomNavViewModel
    }

    @AssistedFactory
    interface ProfileViewModelFactory {
        fun create(userId: String): ProfileViewModel
    }

    @AssistedFactory
    interface CalendarSettingFactory {
        fun create(userId: String): CalendarSettingViewModel
    }

    @AssistedFactory
    interface FoodSearchFactory {
        fun create(
            userId: String,
            mealType: MealType?,
        ): FoodSearchViewModel
    }

    @AssistedFactory
    interface HomeViewModelFactory {
        fun create(userId: String): HomeViewModel
    }

    @AssistedFactory
    interface NutritionViewModelFactory {
        fun create(
            day: Long?,
            @Assisted("user") userId: String?,
            @Assisted("food") foodId: String?,
        ): NutritionViewModel
    }

}