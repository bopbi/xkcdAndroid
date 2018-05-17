package com.arjunalabs.android.xkcdreader

data class XkcdData(
        val month : Int,
        val num : Int,
        val link : String,
        val year : Int,
        val news : String,
        val safe_title : String,
        val transcript : String,
        val alt : String,
        val img : String,
        val title : String,
        val day : Int
)