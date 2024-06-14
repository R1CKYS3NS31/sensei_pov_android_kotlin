package com.example.pov

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.example.data.service.work.initializer.Sync
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Provider


@HiltAndroidApp
class PoVApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var imageLoader: Provider<ImageLoader>
    override fun onCreate() {
        super.onCreate()
        Sync.initializer(context = this)
    }

    override fun newImageLoader(): ImageLoader =
        imageLoader.get()
}