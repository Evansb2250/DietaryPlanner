package com.example.chooseu.navigation.navgraphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import com.example.chooseu.navigation.navgraphs.composable.AuthentificationFlowComposable
import com.example.chooseu.navigation.navgraphs.composable.MainScreenFlowComposable

/*
 * User can either go to the Login or Registration screen from onAppStartUp. Switching between the two screen is done by a tab and logic and no navigation is used.
 *
 * MainScreen takes the user to the Home Screen where there is a bottom Navigation to go to different screens.
 */
@RequiresApi(Build.VERSION_CODES.P)
fun NavGraphBuilder.OnAppStartUpFlow() {

    /***
     * contains login and register screen
     */
    AuthentificationFlowComposable()

    /***
     *  Register a new user
     */
    RegistrationFlow()
    /***
     * directs to landing page of the application.
     */
    MainScreenFlowComposable()
}