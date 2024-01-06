package com.example.googlelightcalendar.core.registration

import javax.inject.Inject
import javax.inject.Singleton

interface UserRegistrationCache {
    fun storeKey(key: RegistrationKeys, value: String)
    fun removeKey(key: RegistrationKeys)
    fun getCache(): Map<String, String>

    fun getKey(key: RegistrationKeys): String?
    fun clearCache()

    fun printToList(): List<String>
}

enum class RegistrationKeys(
    val key: String
) {
    FirstName("firstName"),
    LASTNAME("lastName"),
    EMAIL("email"),
    PASSWORD("password"),
    GENDER("gender"),
    BIRTHDATE("birthday"),
    HEIGHT("height"),
    HEIGHTUNIT("heightUnit"),
    WEIGHT("weight"),
    WEIGHTUNIT("weightUnit"),
    GoalType("goalType"),
    TargetWeight("targetWeight"),
    AccomplishGoalByDate("accomplishGoalByDate"),
    WeeklyTarget("WeeklyTarget")
}

@Singleton
class UserRegistrationCacheImpl @Inject constructor() : UserRegistrationCache {
    private val registrationCache = hashMapOf<String, String>()

    private val firstName: String = "First name: ${registrationCache[RegistrationKeys.FirstName.key] ?: ""} "
    private val lastName: String = "Last name: ${registrationCache[RegistrationKeys.LASTNAME.key] ?: ""} "
    private val email: String = "Email: ${registrationCache[RegistrationKeys.EMAIL.key] ?: ""} "
    private val birthdate: String = "Birthday: ${registrationCache[RegistrationKeys.BIRTHDATE.key] ?: ""} "
    private val gender: String = "Gender: ${registrationCache[RegistrationKeys.GENDER.key]}"
    private val weight: String = "Weight:${registrationCache[RegistrationKeys.WEIGHT.key]} ${registrationCache[RegistrationKeys.WEIGHTUNIT.key]} "
    private val height: String = "Height: ${registrationCache[RegistrationKeys.HEIGHT.key]} ${registrationCache[RegistrationKeys.HEIGHTUNIT.key]}"
    private val goal: String = "Goal: ${registrationCache[RegistrationKeys.GoalType.key]} "
    private val target: String = "Target weight: ${registrationCache[RegistrationKeys.TargetWeight.key]}"
    private val weeklyTarget: String = "${registrationCache[RegistrationKeys.GoalType.key]}  Weekly Target: ${registrationCache[RegistrationKeys.WeeklyTarget.key]} ${registrationCache[RegistrationKeys.WEIGHTUNIT.key]}  per week"
    private val accomplishGoalByDate: String = "Accomplish Goal By: ${registrationCache[RegistrationKeys.AccomplishGoalByDate.key]}"

    override fun storeKey(key: RegistrationKeys, value: String) {
        registrationCache.put(key.key, value)
    }

    override fun removeKey(key: RegistrationKeys) {
        registrationCache.remove(key = key.key)
    }

    override fun getCache(): HashMap<String, String> = registrationCache
    override fun getKey(registrationType: RegistrationKeys): String? {
        return registrationCache[registrationType.key]
    }

    override fun clearCache() {
        registrationCache.clear()
    }

    override fun printToList(): List<String> = mutableListOf(
        firstName,
        lastName,
        email,
        birthdate,
        gender,
        weight,
        height,
        goal,
        accomplishGoalByDate
    ).apply {
        if (registrationCache[RegistrationKeys.WeeklyTarget.key] != null && registrationCache[RegistrationKeys.WeeklyTarget.key]?.trim() != "null") {
            this.add(8, target)
            this.add(9, weeklyTarget)
        }
    }

}

