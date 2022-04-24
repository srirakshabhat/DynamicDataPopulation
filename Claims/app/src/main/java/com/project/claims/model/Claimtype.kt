package com.project.claims.model


import com.google.gson.annotations.SerializedName

data class Claimtype(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)