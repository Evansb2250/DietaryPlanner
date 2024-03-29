package com.example.chooseu.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.chooseu.core.dispatcher_provider.DispatcherProvider
import com.example.chooseu.data.database.GoogleLightCalenderDatabase
import com.example.chooseu.data.rest.api_service.EdamamFoodApiService
import com.example.chooseu.data.rest.api_service.service.account.AccountService
import com.example.chooseu.data.rest.api_service.service.user_table.UserRemoteService
import com.example.chooseu.data.rest.api_service.service.weight_history.BodyMassIndexRemoteService
import com.example.chooseu.repo.UserRepository
import com.example.chooseu.repo.UserRepositoryImpl
import com.example.chooseu.repo.foodRepository.FoodRepository
import com.example.chooseu.repo.foodRepository.FoodRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesUserRepository(
        accountService: AccountService,
        database: GoogleLightCalenderDatabase,
        dispatcherProvider: DispatcherProvider,
        dataStore: DataStore<Preferences>,
        userService: UserRemoteService,
        weightHistoryService: BodyMassIndexRemoteService,
    ): UserRepository {
        return UserRepositoryImpl(
            bmiDao = database.bodyMassIndexDao() ,
            dataStore = dataStore,
            accountService = accountService,
            dispatcherProvider = dispatcherProvider,
            userRemoteDbService = userService,
            bodyMassIndexService = weightHistoryService,
        )
    }


    @Singleton
    @Provides
    fun providesFoodRepository(
        edamamFoodApiService: EdamamFoodApiService,
    ): FoodRepository = FoodRepositoryImp(edamamFoodApiService)
}