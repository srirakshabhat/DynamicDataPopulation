package com.project.claims.model


import com.google.gson.annotations.SerializedName

data class Claimfield(
    @SerializedName("Claimfieldoption")
    val claimfieldoption: List<Claimfieldoption>,
    @SerializedName("created")
    val created: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("isdependant")
    val isdependant: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("required")
    val required: String,
    @SerializedName("type")
    val type: String
)