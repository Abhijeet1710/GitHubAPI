package com.abhijeet_exploring_kotlin_api.githubprofile.model

import com.squareup.moshi.Json

data class UserModel
    (
    @Json(name = "avatar_url") val imageUrl : String,
    val name : String?,
    val location : String?,
    @Json(name = "followers") val followersCount : Int,
    @Json(name = "public_repos") val RepositoryCount : Int,
    val bio : String?,
    @Json(name = "html_url") val htmlPageUrl : String
            )
