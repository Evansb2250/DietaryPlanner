package com.example.googlelightcalendar.repo

import android.util.Log
import com.example.googlelightcalendar.TokenManager
import com.example.googlelightcalendar.api_service.GoogleCalendarClient
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.await

class CalendarRepo(
   val tokenManager: TokenManager,
){
    suspend fun getCalendar(): String {
        val token: String = tokenManager.googleToken.firstOrNull() ?: "Nothing inside"
        Log.d("CalendarRepo", "Auth $token")

        val response = try {
            GoogleCalendarClient.getService().getCalendar("Bearer $token").await()
        } catch (e: Exception) {
            Log.e("CalendarRepo", "Error making API call: ${e.message}")
            null
        }

        if (response != null) {
            Log.d("CalendarRepo", "Response: $response")
            return response
        } else {
            Log.d("CalendarRepo", "API call failed, returning default value")
            return "Default Response"
        }
    }
}