package com.example.local.util

import androidx.room.TypeConverter

internal class AttachmentsConverter {
    @TypeConverter
    fun listToString(attachmentList: List<String>?): String? =
        attachmentList?.let(List<String>::toString)

//    @TypeConverter
//    fun stringToList(attachmentString: String?): List<String>? =
//        attachmentString?.let()
}