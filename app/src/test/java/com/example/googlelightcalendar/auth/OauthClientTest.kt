package com.example.googlelightcalendar.auth

class OauthClientTest {
    /*
    we are going to mock the functionality of the OauthClient

    1. we will verify that registerAuthLauncher was called
    2. we will verify that authorizationLauncher is initialized
    3.we will call attemptAuthorization with differentScope and we will verify if the attemptAuthorization method was called.
    4.we can check the OauthState to see if it contains the right scopes after we verify that the authorizationLauncher.launch(Intent) was called.
    5. we can simulate different response we would get from the ActivityResultLauncher in the LoginScreen and verify that we are getting the right callBacks

    to Mock it  I need to create an Intent and add an extra of
     net.openid.appauth.AuthorizationException
     */
}