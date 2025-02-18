package com.lamprosgk.recipes

import android.app.Application
import com.lamprosgk.data.network.NetworkConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RecipesApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        NetworkConfig.API_KEY = BuildConfig.API_KEY
        NetworkConfig.API_HOST = BuildConfig.API_HOST
        NetworkConfig.BASE_URL = BuildConfig.BASE_URL
    }
}