package com.example.data.common.decoder.di

import com.example.data.common.decoder.StringDecoder
import com.example.data.common.decoder.UriDecoder

abstract class StringDecoderModule {
    /* bind string decoder */
    abstract fun bindsStringDecoder(
        uriDecoder: UriDecoder
    ): StringDecoder
}