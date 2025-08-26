package com.ms.countryapp.util

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ms.countryapp.R
import com.ms.countryapp.data.Country
import java.io.File

class Utility {

    //Read from Json
    fun readJsonFile(context: Context): MutableState<List<Country>> {
        val inputStream = context.resources.openRawResource(R.raw.countryinfo)
        val file = inputStream.bufferedReader().use { it.readText() }
        val listType = object : TypeToken<List<Country>>() {}.type
        val country: List<Country> = Gson().fromJson(file, listType)
        return mutableStateOf(country)
    }

}