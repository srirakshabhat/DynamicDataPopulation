package com.project.claims.model


import com.google.gson.annotations.SerializedName

data class Claim(
    @SerializedName("Claimtype")
    val claimtype: Claimtype,
    @SerializedName("Claimtypedetail")
    val claimtypedetail: List<Claimtypedetail>
)