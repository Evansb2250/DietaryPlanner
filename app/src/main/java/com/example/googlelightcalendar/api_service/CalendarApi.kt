package com.example.googlelightcalendar.api_service

import com.example.googlelightcalendar.core.TokenManager

class CalendarAPI(
    val tokenManager: TokenManager,
){
//    suspend fun getCalendar(): String {
//  //      val token: String = tokenManager.googleToken.firstOrNull() ?: "Nothing inside"
//   //     Log.d("CalendarRepo", "Auth $token")
//
//        val response = try {
//    //        GoogleCalendarClient.getService().getCalendar("Bearer $token").await()
//        } catch (e: Exception) {
//            Log.e("CalendarRepo", "Error making API call: ${e.message}")
//            null
//        }
//
//        if (response != null) {
//            Log.d("CalendarRepo", "Response: $response")
//            return response
//        } else {
//            Log.d("CalendarRepo", "API call failed, returning default value")
//            return "Default Response"
//        }
//    }
}