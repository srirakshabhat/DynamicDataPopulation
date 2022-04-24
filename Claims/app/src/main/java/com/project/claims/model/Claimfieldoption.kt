package com.project.claims.model


import com.google.gson.annotations.SerializedName

data class Claimfieldoption(
    @SerializedName("belongsto")
    val belongsto: Any,
    @SerializedName("claimfield_id")
    val claimfieldId: String,
    @SerializedName("hasmany")
    val hasmany: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("name")
    val name: String
)