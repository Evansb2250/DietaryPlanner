package com.example.googlelightcalendar.api_service

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GoogleCalendarApiService {
    @GET("/calendar/v3/users/me/calendarList/samuelebrandenburg12@gmail.com")
    fun getCalendar(@Header("Authorization") token: String ): Call<String>
                    //@Query("id") email: String): Call<String>
}