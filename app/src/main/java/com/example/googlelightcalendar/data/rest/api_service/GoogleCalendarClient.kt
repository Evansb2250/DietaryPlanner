package com.example.googlelightcalendar.data.rest.api_service

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


const val BASE_URL = "https://www.googleapis.com"
class GoogleCalendarClient {
        private lateinit var instance: GoogleCalendarApiService
        fun getService(): GoogleCalendarApiService {
            if(!::instance.isInitialized){
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()

                instance = retrofit.create(GoogleCalendarApiService::class.java)
            }

            return instance
        }
}