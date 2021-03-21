package com.abhijeet_exploring_kotlin_api.githubprofile.service

import com.abhijeet_exploring_kotlin_api.githubprofile.service.GitHubService.Companion.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

fun createGitHubService() : GitHubService {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            MoshiConverterFactory.create(moshi)
        )
        .build()
        .create(GitHubService::class.java)
}

