package com.baehyeonwoo.sample

import io.github.monun.tap.config.Config
import io.github.monun.tap.config.ConfigSupport
import java.io.File

object SampleConfig {
    @Config
    var administrator = arrayListOf(
        "5082c832-7f7c-4b04-b0c7-2825062b7638"
    )

    fun load(configFile: File) {
        ConfigSupport.compute(this, configFile)
    }
}