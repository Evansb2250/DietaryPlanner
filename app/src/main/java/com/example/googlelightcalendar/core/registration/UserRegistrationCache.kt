package com.example.googlelightcalendar.core.registration

import javax.inject.Inject
import javax.inject.Singleton

interface UserRegistrationCache {
    fun storeKey(key: RegistrationKeys, value: String)
    fun removeKey(key: RegistrationKeys)
    fun getCache(): HashMap<String, String>

    fun getKey(key: RegistrationKeys): String?

    fun clearCache()
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
}

@Singleton
class UserRegistrationCacheImpl @Inject constructor() : UserRegistrationCache {
    private val registrationCache = hashMapOf<String, String>()
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

}