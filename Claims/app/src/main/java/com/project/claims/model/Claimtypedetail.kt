package com.project.claims.model


import com.google.gson.annotations.SerializedName

data class Claimtypedetail(
    @SerializedName("Claimfield")
    val claimfield: Claimfield,
    @SerializedName("claimfield_id")
    val claimfieldId: String,
    @SerializedName("claimtype_id")
    val claimtypeId: String,
    @SerializedName("id")
    val id: String
)