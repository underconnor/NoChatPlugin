/*
 * Copyright (c) 2021 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/gpl-3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baehyeonwoo.nochat

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/***
 * @author BaeHyeonWoo
 */

class NoChatPluginMain : JavaPlugin() {

    companion object {
        lateinit var instance: NoChatPluginMain
            private set
    }

    private val configFile = File(dataFolder, "config.yml")

    override fun onEnable() {
        instance = this
        NoChatConfig.load(configFile)
        val enabled = config.getBoolean("enabled")

        if (enabled) {
            server.pluginManager.registerEvents(NoChatEvent(), this)
        }

        val sm = server.scoreboardManager
        val sc = sm.mainScoreboard

        val administrator = sc.getTeam("admin")

        if (administrator == null) {
            sc.registerNewTeam("admin")
        }

        administrator?.setAllowFriendlyFire(false)
        administrator?.setCanSeeFriendlyInvisibles(false)
        administrator?.color(NamedTextColor.RED)

        NoChatKommand.noChatKommand()
    }
}