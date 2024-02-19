package com.example.chooseu.core.registration.cache

import com.example.chooseu.core.registration.cache.keys.RegistrationKeys
import javax.inject.Inject
import javax.inject.Singleton

interface UserRegistrationCache {
    fun storeKey(key: RegistrationKeys, value: String)
    fun removeKey(key: RegistrationKeys)
    fun getCache(): Map<String, String>

    fun getKey(key: RegistrationKeys): String?
    fun clearCache()

    fun storeKey(map: Map<String, String>)
    fun printToList(): List<String>
}

@Singleton
class UserRegistrationCacheImpl @Inject constructor() : UserRegistrationCache {
    private val registrationCache = hashMapOf<String, String>()


    override fun storeKey(key: RegistrationKeys, value: String) {
        registrationCache.put(key.key, value)
    }

    override fun storeKey(map: Map<String, String>) {
        map.keys.forEach { key ->
            map[key]?.let { value ->
                registrationCache.put(key, value)
            }
        }

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

    override fun printToList(): List<String> {
        return mutableListOf(
            "${RegistrationKeys.FirstName.label} ${registrationCache[RegistrationKeys.FirstName.key]}",
            "${RegistrationKeys.LastName.label} ${registrationCache[RegistrationKeys.LastName.key]}",
            "${RegistrationKeys.EMAIL.label} ${registrationCache[RegistrationKeys.EMAIL.key]}",
            "${RegistrationKeys.BIRTHDATE.label} ${registrationCache[RegistrationKeys.BIRTHDATE.key]}",
            "${RegistrationKeys.GENDER.label} ${registrationCache[RegistrationKeys.GENDER.key]}",
            "${RegistrationKeys.HEIGHT.label} ${registrationCache[RegistrationKeys.HEIGHT.key]}",
            "${RegistrationKeys.HEIGHT_METRIC.label} ${registrationCache[RegistrationKeys.HEIGHT_METRIC.key]}",
            "${RegistrationKeys.WEIGHT.label} ${registrationCache[RegistrationKeys.WEIGHT.key]}",
            "${RegistrationKeys.WEIGHTUNIT.label} ${registrationCache[RegistrationKeys.WEIGHTUNIT.key]}",
            "${RegistrationKeys.GoalType.label} ${registrationCache[RegistrationKeys.GoalType.key]}",
            "${RegistrationKeys.AccomplishGoalByDate.label} ${registrationCache[RegistrationKeys.AccomplishGoalByDate.key]}"
        ).apply {
            if (registrationCache[RegistrationKeys.WeeklyTarget.key] != null && registrationCache[RegistrationKeys.WeeklyTarget.key]?.trim() != "null") {
                this.add(
                    index = 8,
                    element = "${RegistrationKeys.TargetWeight.label} ${registrationCache[RegistrationKeys.TargetWeight.key]}",
                )
                this.add(
                    index = 9,
                    element = "${RegistrationKeys.WeeklyTarget.label} ${registrationCache[RegistrationKeys.WeeklyTarget.key]}",
                )
            }
        }
    }
}

