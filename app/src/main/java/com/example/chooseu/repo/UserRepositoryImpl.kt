package com.example.chooseu.repo

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.chooseu.common.DataStoreKeys
import com.example.chooseu.common.DataStoreKeys.BMI_STORED_DATE
import com.example.chooseu.common.DataStoreKeys.BMI_VALUE
import com.example.chooseu.common.DataStoreKeys.USER_HEIGHT
import com.example.chooseu.common.DataStoreKeys.USER_HEIGHT_METRIC
import com.example.chooseu.common.DataStoreKeys.USER_ID
import com.example.chooseu.common.DataStoreKeys.USER_WEIGHT
import com.example.chooseu.common.DataStoreKeys.USER_WEIGHT_METRIC
import com.example.chooseu.core.dispatcher_provider.DispatcherProvider
import com.example.chooseu.core.cache.keys.RegistrationKeys
import com.example.chooseu.data.database.dao.BMIDao
import com.example.chooseu.data.database.models.BMIEntity
import com.example.chooseu.data.database.models.toBodyMassIndexList
import com.example.chooseu.data.rest.api_service.service.account.AccountService
import com.example.chooseu.data.rest.api_service.service.user_table.UserRemoteService
import com.example.chooseu.data.rest.api_service.service.weight_history.BodyMassIndexRemoteService
import com.example.chooseu.domain.BodyMassIndex
import com.example.chooseu.domain.CurrentUser
import com.example.chooseu.utils.AsyncResponse
import com.example.chooseu.utils.DataStoreUtil.clearUserData
import com.example.chooseu.utils.DataStoreUtil.storeUserData
import com.example.chooseu.utils.DateUtility
import com.google.gson.JsonObject
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.DocumentList
import io.appwrite.models.Session
import io.appwrite.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import io.appwrite.models.User as AppWriteUser

sealed class UpdateResult {
    data class Success(val data: Any? = null, val message: String) : UpdateResult()
    data class Failed(val message: String) : UpdateResult()
}


@Singleton
class UserRepositoryImpl @Inject constructor(
    private val bmiDao: BMIDao,
    private val accountService: AccountService,
    private val dataStore: DataStore<Preferences>,
    private val dispatcherProvider: DispatcherProvider,
    private val userRemoteDbService: UserRemoteService,
    private val bodyMassIndexService: BodyMassIndexRemoteService,
) : UserRepository {

    override val currentUser: Flow<CurrentUser?> = combine(
        flow = dataStore.data,
        flow2 = bmiDao.getCurrentBMI()
    ) { dataStoreData, currentBMIData ->

        val firstName = dataStoreData[DataStoreKeys.USER_FIRST_NAME] ?: return@combine null
        val lastName = dataStoreData[DataStoreKeys.USER_LAST_NAME] ?: return@combine null
        val gender = dataStoreData[DataStoreKeys.USER_GENDER] ?: return@combine null
        val email = dataStoreData[DataStoreKeys.USER_EMAIL] ?: return@combine null
        val birthDate = dataStoreData[DataStoreKeys.USER_BIRTH_DATE] ?: return@combine null
        val userData = currentBMIData ?: return@combine null

        CurrentUser(
            userName = email,
            name = firstName,
            lastName = lastName,
            gender = gender,
            email = email,
            birthdate = birthDate,
            heightMetric = userData.heightMetric,
            height = userData.height,
            weightMetric = userData.weightMetric,
            weight = userData.weight
        )
    }

    override suspend fun signIn(userName: String, password: String): AsyncResponse<String> =
        withContext(dispatcherProvider.io) {
            try {
                storeSessionExpirationDate(accountService.login(userName, password))

                val result = handleLoggedInResponse(accountService.getLoggedInUser())

                //if storing information failed we want to log the user out of the session
                if (result is AsyncResponse.Failed) {
                    clearPrefsOnSignOut()
                }

                result
            } catch (e: AppwriteException) {
                AsyncResponse.Failed(
                    data = null,
                    message = e.message,
                )
            }
        }

    private suspend fun storeSessionExpirationDate(session: AsyncResponse<Session>) {
        if (session is AsyncResponse.Success) {
            dataStore.edit {
                it[DataStoreKeys.USER_SESSION_EXPIRATION] = session.data?.expire ?: ""
            }
        }
    }

    private suspend fun handleLoggedInResponse(result: AsyncResponse<User<Map<String, Any>>?>): AsyncResponse<String> =
        when (result) {
            is AsyncResponse.Failed -> {
                AsyncResponse.Failed(
                    data = null,
                    message = result.message ?: "Failed to Login",
                )
            }
            is AsyncResponse.Success -> {
                storeUserData(result.data)
            }
        }

    //all I want to know if logging in, getting the user data and storing it was successful.
    private suspend fun storeUserData(userData: User<Map<String, Any>>?): AsyncResponse<String> {
        val result = coroutineScope {

            val personalInfo = async { userRemoteDbService.fetchUserDetails(userData!!.id) }.await()
            val fetchHistoryResponse =
                async { bodyMassIndexService.fetchUserWeightHistory(userData!!.id) }.await()

            when (fetchHistoryResponse) {
                is AsyncResponse.Failed -> {
                    return@coroutineScope AsyncResponse.Failed(
                        data = "Failed",
                        message = fetchHistoryResponse.message
                    )
                }

                is AsyncResponse.Success -> {
                    storeWeightHistoryInRoom(fetchHistoryResponse.data!!)
                }
            }
            return@coroutineScope dataStore.storeUserData(personalInfo)
        }

        return result
    }


    private suspend fun storeWeightHistoryInRoom(weightHistory: DocumentList<Map<String, Any>>) {
        val listOfBMI = mutableListOf<BMIEntity>().apply {
            weightHistory.documents.forEach { bmiInfo ->
                this.add(
                    BMIEntity(
                        documentId = bmiInfo.id,
                        userId = bmiInfo.data[DataStoreKeys.USER_ID.name] as String,
                        weight = bmiInfo.data[USER_WEIGHT.name]?.toString()?.toDoubleOrNull()
                            ?: 0.0,
                        weightMetric = bmiInfo.data[USER_WEIGHT_METRIC.name] as String,
                        height = bmiInfo.data[USER_HEIGHT.name]?.toString()?.toDoubleOrNull()
                            ?: 0.0,
                        heightMetric = bmiInfo.data[USER_HEIGHT_METRIC.name] as String,
                        bmi = bmiInfo.data[BMI_VALUE.name].toString().toDoubleOrNull() ?: 0.0,
                        dateAsInteger = bmiInfo.data[BMI_STORED_DATE.name].toString().toDouble().toLong(),
                    )
                )
            }
        }
        bmiDao.insertListOfBMI(listOfBMI)
    }

    override suspend fun clearPrefsOnSignOut() {
        withContext(dispatcherProvider.io) {
            dataStore.clearUserData()
            bmiDao.deleteAll()
            accountService.logout()
        }
    }

    override suspend fun getBMIHistory(): List<BodyMassIndex> {
        return withContext(dispatcherProvider.io) {
            val userId =
                dataStore.data.firstOrNull()?.get(USER_ID) ?: return@withContext emptyList()
            return@withContext bmiDao.getBMIHistory(userId).toBodyMassIndexList()
        }
    }

    override suspend fun createUserInServer(userInfo: Map<String, String>): UpdateResult =
        withContext(dispatcherProvider.io) {
            return@withContext try {
                //Don't remove throws error if any of the fields in the map is null
                checkUserInfo(userInfo)

                //if we are able to create an account, we will get a User object
                val registerUserResponse = accountService.registerUser(
                    email = userInfo[RegistrationKeys.EMAIL.key]!!,
                    name = "${userInfo[RegistrationKeys.FirstName.key]} ${userInfo[RegistrationKeys.LastName.key]}",
                    password = userInfo[RegistrationKeys.PASSWORD.key]!!,
                )

                handleUserRegistrationResponse(registerUserResponse, userInfo)

            } catch (e: AppwriteException) {
                UpdateResult.Failed(
                    message = e.message ?: "Failed AppwriteException"
                )
            } catch (e: IllegalArgumentException) {
                UpdateResult.Failed(
                    message = e.message ?: "Failed IllegalArgumentException"
                )
            } catch (e: NullPointerException) {
                UpdateResult.Failed(
                    message = e.message ?: "Failed NullPointerException"
                )
            }
        }

    override suspend fun addNewBodyMassIndexToServer(
        weight: Double,
        weightMetric: String,
        height: Double,
        heightMetric: String
    ): UpdateResult {
        return withContext(Dispatchers.IO) {
            val pref = dataStore.data.firstOrNull() ?: return@withContext UpdateResult.Failed(
                message = "Missing information on device"
            )

            val userId = pref.get(DataStoreKeys.USER_ID)
                ?: return@withContext UpdateResult.Failed("userId not found")


            val todayDate = DateUtility.calculateDateAsLong()

            when (val bmiEntity = bmiDao.dateAlreadyExist(todayDate)) {
                null -> {
                    // if our BMI change isn't already in our system we add it to the server as a new entry
                    bodyMassIndexService.createNewDocument(
                        userId = userId,
                        weight = weight,
                        weightMetric = weightMetric,
                        height = height,
                        heightMetric = heightMetric,
                        bmi = 4.0,
                        dateInteger = todayDate
                    )
                }

                else -> {
                    //The BMI already exist so we need to update the document with the new metrics
                    bodyMassIndexService.updateDocument(
                        documentId = bmiEntity.documentId,
                        createBMIJsonObject(
                            weight = weight,
                            weightMetric = weightMetric,
                            height = height,
                            heightMetric = heightMetric
                        )
                    )
                }
            }


            val serverResponse = bodyMassIndexService.fetchUserWeightHistory(userId)

            handleUpdateDocumentRequest(serverResponse)
        }
    }

    private suspend fun handleUpdateDocumentRequest(
        updateDocumentResponse: AsyncResponse<DocumentList<Map<String, Any>>>
    ): UpdateResult {
        return when (updateDocumentResponse) {
            is AsyncResponse.Failed -> {
                UpdateResult.Failed(
                    message = updateDocumentResponse.message ?: "Couldn't update try later"
                )
            }

            is AsyncResponse.Success -> {
                updateDocumentResponse.data?.let { doc ->
                    storeWeightHistoryInRoom(doc)
                    val successResult = UpdateResult.Success(message = "data updated")
                    return successResult
                }

                return UpdateResult.Failed(message = "Couldn't update document; it was null. Please try later.")
            }
        }
    }

    private fun createBMIJsonObject(
        weight: Double,
        weightMetric: String,
        height: Double,
        heightMetric: String
    ): JsonObject {
        return JsonObject().apply {
            addProperty(USER_WEIGHT.name, weight.toString())
            addProperty(USER_WEIGHT_METRIC.name, weightMetric)
            addProperty(USER_HEIGHT.name, height.toString())
            addProperty(USER_HEIGHT_METRIC.name, heightMetric)
        }
    }

    private suspend fun handleUserRegistrationResponse(
        registerUserStatus: AsyncResponse<AppWriteUser<Map<String, Any>>?>,
        userInfo: Map<String, String>
    ): UpdateResult {
        return when (registerUserStatus) {
            is AsyncResponse.Failed -> {
                UpdateResult.Failed(
                    message = registerUserStatus.message ?: "Unknown error while handling response"
                )
            }

            is AsyncResponse.Success -> {
                //add it to the database

                val result = coroutineScope {
                    async {
                        userRemoteDbService.add(
                            userId = registerUserStatus.data!!.id,
                            firstName = userInfo[RegistrationKeys.FirstName.key]!!,
                            lastName = userInfo[RegistrationKeys.LastName.key]!!,
                            birthDate = userInfo[RegistrationKeys.BIRTHDATE.key]!!,
                            email = userInfo[RegistrationKeys.EMAIL.key]!!,
                            gender = userInfo[RegistrationKeys.GENDER.key]!!,
                        )
                    }.await()

                    async {
                        bodyMassIndexService.createNewDocument(
                            userId = registerUserStatus.data!!.id,
                            weight = userInfo[RegistrationKeys.WEIGHT.key]!!.toDouble(),
                            weightMetric = userInfo[RegistrationKeys.WEIGHTUNIT.key]!!,
                            height = userInfo[RegistrationKeys.HEIGHT.key]!!.toDouble(),
                            heightMetric = userInfo[RegistrationKeys.HEIGHT_METRIC.key]!!,
                            bmi = 4.0,
                            dateInteger = DateUtility.calculateDateAsLong()
                        )
                    }.await()

                    //used to end session, but no data is saved in dataStore
                    clearPrefsOnSignOut()
                    UpdateResult.Success(message = "Account Created !!")
                }
                result
            }
        }
    }

    //Throws IllegalArgumentException if any field isn't completed.
    fun checkUserInfo(userInfo: Map<String, String>) {
        val email = userInfo[RegistrationKeys.EMAIL.key]
        val password = userInfo[RegistrationKeys.PASSWORD.key]
        val firstName = userInfo[RegistrationKeys.FirstName.key]
        val lastName = userInfo[RegistrationKeys.LastName.key]

        val gender = userInfo[RegistrationKeys.GENDER.key]
        val height = userInfo[RegistrationKeys.HEIGHT.key]
        val heightMetric = userInfo[RegistrationKeys.HEIGHT_METRIC.key]
        val weight = userInfo[RegistrationKeys.WEIGHT.key]
        val weightMetric = userInfo[RegistrationKeys.LastName.key]


        // Check if any of the fields is null or empty using require
        require(!email.isNullOrEmpty()) { "Email cannot be null or empty" }
        require(!password.isNullOrEmpty()) { "Password cannot be null or empty" }
        require(!firstName.isNullOrEmpty()) { "First name cannot be null or empty" }
        require(!lastName.isNullOrEmpty()) { "Last name cannot be null or empty" }
        require(!gender.isNullOrEmpty()) { "gender cannot be null or empty" }
        require(!height.isNullOrEmpty()) { "height cannot be null or empty" }
        require(!heightMetric.isNullOrEmpty()) { "heightMetric cannot be null or empty" }
        require(!weight.isNullOrEmpty()) { "weight cannot be null or empty" }
        require(!weightMetric.isNullOrEmpty()) { "weightMetric cannot be null or empty" }
    }
}