package com.example.chooseu.common

class Constants {
    companion object {
        val SHARED_PREFERENCES_NAME = "AUTH_STATE_PREFERENCE"
        val AUTH_STATE = "AUTH_STATE"

        val SCOPE_PROFILE = "profile"
        val SCOPE_EMAIL = "email"
        val SCOPE_OPENID = "openid"

        val CALENDAR_EVENTS = "https://www.googleapis.com/auth/calendar.events.readonly"
        val CALENDAR_READ_ONLY = "https://www.googleapis.com/auth/calendar.readonly"
        val CALENDAR_SCOPE = "https://www.googleapis.com/auth/calendar"

        val CLIENT_SECRET =
            "436600699866-mb2h26828pgkgs44glut32iht394ntja.apps.googleusercontent.com"

        val CODE_VERIFIER_CHALLENGE_METHOD = "S256"
        val MESSAGE_DIGEST_ALGORITHM = "SHA-256"

        val URL_AUTHORIZATION = "https://accounts.google.com/o/oauth2/v2/auth"
        val URL_TOKEN_EXCHANGE = "https://www.googleapis.com/oauth2/v4/token"
        val URL_AUTH_REDIRECT = "com.example.googlelightcalendar:/oauth2redirect"
        val URL_API_CALL = "https://www.googleapis.com/drive/v2/files"
        val URL_LOGOUT = "https://accounts.google.com/o/oauth2/revoke?token="

        //Edamams API Keys
        /*
         */
        const val EDAMAM_APPLICATION_ID = "6d8a5a04"
        const val EDAMAM_APPLICATION_KEY = "5b9a69743d6032a52c2e611b683563ff"

        // AppWrite info
        const val appwriteEndPoint = "https://cloud.appwrite.io/v1"
        const val appWriteProjectId = "65cc071f270d4688d790"

        val URL_LOGOUT_REDIRECT = "com.ptruiz.authtest:/logout"

        const val googleSignNotAvailable = "Google Sign in is not available"
    }
}