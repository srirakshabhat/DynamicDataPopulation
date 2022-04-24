package com.project.claims.model


import com.google.gson.annotations.SerializedName

data class Claims(
    @SerializedName("Claims")
    val claims: List<Claim>,
    @SerializedName("Reason")
    val reason: String,
    @SerializedName("Result")
    val result: Boolean
)