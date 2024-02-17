package com.example.chooseu.common

import android.content.Context
import com.example.chooseu.data.rest.api_service.service.account.AccountService
import com.example.chooseu.data.rest.api_service.service.idea.IdeaService
import io.appwrite.Client

object Appwrite {
    lateinit var client: Client
    lateinit var account: AccountService
    internal lateinit var ideas: IdeaService
    fun init(context: Context)  {
        if(!::account.isInitialized){
            client = Client(context)
                .setEndpoint("https://cloud.appwrite.io/v1")
                .setProject("65cc071f270d4688d790")
       //     ideas = IdeaService(client)
            account = AccountService(client)
        }
    }
}
