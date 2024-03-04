package com.example.chooseu.repo

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.chooseu.auth.OauthClient
import com.example.chooseu.common.DataStoreKeys
import com.example.chooseu.core.TokenManager
import com.example.chooseu.core.dispatcher_provider.DispatcherProvider
import com.example.chooseu.core.registration.cache.keys.RegistrationKeys
import com.example.chooseu.core.registration.state.RegisterGoalStates
import com.example.chooseu.data.database.dao.UserDao
import com.example.chooseu.data.database.models.toUser
import com.example.chooseu.data.rest.api_service.service.account.AccountService
import com.example.chooseu.data.rest.api_service.service.user_table.UserRemoteService
import com.example.chooseu.data.rest.api_service.service.weight_history.BodyMassIndexRemoteService
import com.example.chooseu.domain.CurrentUser
import com.example.chooseu.utils.AsyncResponse
import com.example.chooseu.utils.DataStoreUtil.clearUserData
import com.example.chooseu.utils.DataStoreUtil.storeUserData
import com.example.chooseu.utils.DataStoreUtil.toCurrentUser
import com.google.gson.JsonObject
import io.appwrite.ID
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.Document
import io.appwrite.models.Session
import io.appwrite.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import io.appwrite.models.User as AppWriteUser

sealed class UpdateResult {
    data class Success(val data: String) : UpdateResult()
    data class Failed(val message: String) : UpdateResult()
}


@Singleton
class UserRepositoryImpl @Inject constructor(
    private val googleOauthClient: Lazy<OauthClient>,
    private val userDao: UserDao,
    private val accountService: AccountService,
    private val dataStore: DataStore<Preferences>,
    private val tokenManager: TokenManager,
    private val dispatcherProvider: DispatcherProvider,
    private val userRemoteDbService: UserRemoteService,
    private val weightHistoryService: BodyMassIndexRemoteService,
) : UserRepository {

    override val currentUser: Flow<CurrentUser?> = dataStore.toCurrentUser()


    @Deprecated("App Write will take care of this")

    // Aysnc response is received in the ActivityResultLauncher in the loginScreen
    override fun attemptAuthorization(
        authorizationScopes: Array<String>
    ) {
        googleOauthClient.value.attemptAuthorization(authorizationScopes)
    }

    @Deprecated("App Write will take care of this")

    //Registers the googleOauthClient to the activity launcher the googleOauthClient is a singleton, and it survives while mainActivity is alive.
    override fun registerAuthLauncher(launcher: ActivityResultLauncher<Intent>) {
        googleOauthClient.value.registerAuthLauncher(launcher)
    }

    override suspend fun signIn(userName: String, password: String): AsyncResponse<Unit> =
        withContext(dispatcherProvider.io) {
            try {
                storeSessionExpirationDate(accountService.login(userName, password))

                val result = handleLoggedInResponse(accountService.getLoggedInUser())

                //if storing information failed we want to log the user out of the session
                if (result is AsyncResponse.Failed) {
                    clearPrefsAndSignOut()
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

    private suspend fun handleLoggedInResponse(result: AsyncResponse<User<Map<String, Any>>?>): AsyncResponse<Unit> =
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
    private suspend fun storeUserData(userData: User<Map<String, Any>>?): AsyncResponse<Unit> {
        val doc = userRemoteDbService.fetchUserDetails(userData!!.id)
        return dataStore.storeUserData(doc)
    }


    override suspend fun clearPrefsAndSignOut() {
        withContext(dispatcherProvider.io) {
            dataStore.clearUserData()
            accountService.logout()
        }
    }

    @Deprecated("App Write will take care of this")

    override suspend fun handleAuthorizationResponse(
        intent: Intent,
        authorizationResponse: AuthorizationResponse?,
        error: AuthorizationException?,
    ): AuthorizationResponseStates {
        return withContext(dispatcherProvider.io) {
            val asyncResponse = googleOauthClient.value.handleAuthorizationResponse(
                intent = intent,
                authorizationResponse = authorizationResponse,
                error = error,
            )
            suspendCancellableCoroutine { continuation ->
                when (asyncResponse) {
                    is AsyncResponse.Failed<CurrentUser?> -> {
                        continuation.resume(
                            AuthorizationResponseStates.FailedResponsState(
                                asyncResponse.message ?: "Failed"
                            )
                        )
                    }

                    is AsyncResponse.Success<CurrentUser?> -> {
                        val user =
                            userDao.getUserFromGmailSignIn(asyncResponse.data?.userName ?: "")
                                ?.toUser()

                        if (user != null) {
                            continuation.resume(
                                AuthorizationResponseStates.SuccessResponseState(
                                    email = user.userName,
                                    name = user.name,
                                )
                            )
                        } else {
                            // Have user register account.
                            continuation.resume(
                                AuthorizationResponseStates.FirstTimeUserState(
                                    asyncResponse.data!!.userName,
                                    asyncResponse.data.name,
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    @Deprecated("App Write will take care of this")

    override suspend fun handleSignUpResponse(
        intent: Intent,
        authorizationResponse: AuthorizationResponse?,
        error: AuthorizationException?
    ): AuthorizationResponseStates {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(userInfo: Map<String, String>): AsyncResponse<RegisterGoalStates> =
        withContext(dispatcherProvider.io) {
            return@withContext try {
                //Don't remove throws error if any of the fields in the map is null
                checkUserInfo(userInfo)

                //User Id that we need to add for an account and User table
                val userID = ID.unique()

                //if we are able to create an account, we will get a User object
                val registerUserResponse = accountService.registerUser(
                    userId = userID,
                    email = userInfo[RegistrationKeys.EMAIL.key]!!,
                    name = "${userInfo[RegistrationKeys.FirstName.key]} ${userInfo[RegistrationKeys.LastName.key]}",
                    password = userInfo[RegistrationKeys.PASSWORD.key]!!,
                )

                handleUserRegistrationResponse(registerUserResponse, userInfo)

            } catch (e: IllegalArgumentException) {
                AsyncResponse.Failed(
                    data = RegisterGoalStates.CreationError(
                        e.message ?: "couln't create account"
                    ),
                    message = null
                )
            } catch (e: NullPointerException) {
                AsyncResponse.Failed(
                    data = RegisterGoalStates.CreationError(
                        e.message ?: "couln't create account"
                    ),
                    message = null
                )
            }
        }

    override suspend fun updateUserInfo(
        weight: Double,
        weightMetric: String,
        height: Double,
        heightMetric: String
    ): UpdateResult {
        return withContext(Dispatchers.IO) {
            val pref  = dataStore.data.firstOrNull()
            val documentId = pref?.get(DataStoreKeys.USER_DOC_ID)
                ?: return@withContext UpdateResult.Failed("Doc Id not found")
            val userId = pref.get(DataStoreKeys.USER_ID) ?: return@withContext UpdateResult.Failed("userId not found")

            weightHistoryService.add(
                userId,
                weight,
                weightMetric,
                calculateDateAsLong()
            )

            val serverResponse = userRemoteDbService.updateDocument(
                documentId,
                data = createJsonObject(
                    weight,
                    weightMetric,
                    height,
                    heightMetric
                ),
            )
            handleUpdateDocumentRequest(serverResponse)
        }
    }

    private fun calculateDateAsLong(): Long {
        val date = LocalDate.now()
        val instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant()
        return instant.toEpochMilli()
    }

    private suspend fun handleUpdateDocumentRequest(
        updateDocumentResponse: AsyncResponse<Document<Map<String, Any>>>
    ): UpdateResult {
        return when (updateDocumentResponse) {
            is AsyncResponse.Failed -> {
                UpdateResult.Failed(
                    message = updateDocumentResponse.message ?: "Couldn't update try later"
                )
            }

            is AsyncResponse.Success -> {
                updateDocumentResponse.data?.let { doc ->
                    dataStore.storeUserData(doc)
                    val successResult = UpdateResult.Success(data = "data updated")
                    return successResult
                }

                return UpdateResult.Failed(message = "Couldn't update document; it was null. Please try later.")
            }
        }
    }

    private fun createJsonObject(
        weight: Double,
        weightMetric: String,
        height: Double,
        heightMetric: String
    ): JsonObject {
        return JsonObject().apply {
            addProperty(DataStoreKeys.USER_WEIGHT.name, weight.toString())
            addProperty(DataStoreKeys.USER_WEIGHT_METRIC.name, weightMetric)
            addProperty(DataStoreKeys.USER_HEIGHT.name, height.toString())
            addProperty(DataStoreKeys.USER_HEIGHT_METRIC.name, heightMetric)
        }
    }

    private suspend fun handleUserRegistrationResponse(
        registerUserStatus: AsyncResponse<AppWriteUser<Map<String, Any>>?>,
        userInfo: Map<String, String>
    ): AsyncResponse<RegisterGoalStates> {
        return when (registerUserStatus) {
            is AsyncResponse.Failed -> {
                AsyncResponse.Failed(
                    data = RegisterGoalStates.CreationError(
                        registerUserStatus.message ?: "Couldn't create account"
                    ),
                    message = null
                )
            }

            is AsyncResponse.Success -> {
                //add it to the database
                userRemoteDbService.add(
                    userId = registerUserStatus.data!!.id,
                    firstName = userInfo[RegistrationKeys.FirstName.key]!!,
                    lastName = userInfo[RegistrationKeys.LastName.key]!!,
                    birthDate = userInfo[RegistrationKeys.BIRTHDATE.key]!!,
                    height = userInfo[RegistrationKeys.HEIGHT.key]!!.toDouble(),
                    heightMetric = userInfo[RegistrationKeys.HEIGHT_METRIC.key]!!,
                    weight = userInfo[RegistrationKeys.WEIGHT.key]!!.toDouble(),
                    weightMetric = userInfo[RegistrationKeys.WEIGHTUNIT.key]!!,
                    email = userInfo[RegistrationKeys.EMAIL.key]!!,
                    gender = userInfo[RegistrationKeys.GENDER.key]!!,
                )
                //used to end session, but no data is saved in dataStore
                clearPrefsAndSignOut()
                AsyncResponse.Success(data = RegisterGoalStates.AccountCreated)
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