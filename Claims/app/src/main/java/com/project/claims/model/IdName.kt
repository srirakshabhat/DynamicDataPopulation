package com.project.claims.model

data class IdName(val id:String, val name:String){
    override fun toString():String {
        return name
    }
}

