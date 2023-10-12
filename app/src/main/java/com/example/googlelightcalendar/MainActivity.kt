package com.example.googlelightcalendar

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.googlelightcalendar.ui.theme.GoogleLightCalendarTheme
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException

const val REQ_ONE_TAP = 100

class MainActivity : ComponentActivity() {

    private val TAG = "GoogleSignInDebug"
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.client_web_id))
                    .setFilterByAuthorizedAccounts(false).build())
            .build()


        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    Log.e(TAG, "attempting to sign in")
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d(TAG, " something failed ${e.localizedMessage}")
            }




    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken
            val username = credential.id
            val password = credential.password
            when {
                idToken != null -> {
                    // Got an ID token from Google. Use it to authenticate
                    // with your backend.
                    Log.d(TAG, "Got ID token ${idToken}.")
                }
                password != null -> {
                    // Got a saved username and password. Use them to authenticate
                    // with your backend.
                    Log.d(TAG, "Got password.")
                }
                else -> {
                    // Shouldn't happen.
                    Log.d(TAG, "No ID token or password!")
                }
            }
        } catch (e: ApiException) {
            // ...
        }
    }



}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GoogleLightCalendarTheme {
        Greeting("Android")
    }
}