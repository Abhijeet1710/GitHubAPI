package com.abhijeet_exploring_kotlin_api.githubprofile.service

import com.abhijeet_exploring_kotlin_api.githubprofile.model.UserModel
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username : String) : UserModel

    companion object {
        const val BASE_URL = "https://api/github.com/"
    }

}