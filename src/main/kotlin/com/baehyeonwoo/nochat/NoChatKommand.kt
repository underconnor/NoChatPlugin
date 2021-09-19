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

import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.HandlerList
import org.bukkit.plugin.Plugin

/***
 * @author BaeHyeonWoo
 */

object NoChatKommand {
    private fun getInstance(): Plugin {
        return NoChatPluginMain.instance
    }

    private val config = getInstance().config

    private val server = getInstance().server

    fun noChatKommand() {
        getInstance().kommand {
            register("nochat") {
                executes {
                    sender.sendMessage(text("Usage: /nochat <status/on/off>"))
                }
                then("status") {
                    executes {
                        val enabled = config.getBoolean("enabled")
                        sender.sendMessage(text("Current NoChat Status: $enabled"))
                    }
                }
                then("on") {
                    executes {
                        val enabled = config.getBoolean("enabled")
                        if (enabled) {
                            sender.sendMessage(text("NoChat is Already Enabled.", NamedTextColor.RED))
                        } else {
                            config.set("enabled", true)
                            getInstance().saveConfig()
                            server.pluginManager.registerEvents(NoChatEvent(), getInstance())
                            sender.sendMessage(text("NoChat is Now Enabled!", NamedTextColor.GREEN))
                        }
                    }
                }
                then("off") {
                    executes {
                        val enabled = config.getBoolean("enabled")
                        if (!enabled) {
                            sender.sendMessage(text("NoChat is Already Disabled.", NamedTextColor.RED))
                        } else {
                            config.set("enabled", false)
                            getInstance().saveConfig()
                            HandlerList.unregisterAll(getInstance())
                            sender.sendMessage(text("NoChat is Now Disabled!", NamedTextColor.GREEN))
                        }
                    }
                }
            }
        }
    }
}