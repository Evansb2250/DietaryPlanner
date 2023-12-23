package com.example.googlelightcalendar.core.registration

import javax.inject.Inject
import javax.inject.Singleton

interface UserRegistrationCache {
    fun storeKey(key: RegistrationKeys, value: String)
    fun removeKey(key: RegistrationKeys)
    fun getCache(): HashMap<String, Any>

    fun clearCache()
}

enum class RegistrationKeys(
    val label: String
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
}

@Singleton
class UserRegistrationCacheImpl @Inject constructor() : UserRegistrationCache {
    private val registrationCache = hashMapOf<String, Any>()
    override fun storeKey(key: RegistrationKeys, value: String) {
        registrationCache.put(key.label, value)
    }

    override fun removeKey(key: RegistrationKeys) {
        registrationCache.remove(key = key.label)
    }

    override fun getCache(): HashMap<String, Any> = registrationCache

    override fun clearCache() {
        registrationCache.clear()
    }

}