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

import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import org.bukkit.command.CommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin

/***
 * @author BaeHyeonWoo
 */

class NoChatEvent : Listener {
    private fun getInstance(): Plugin {
        return NoChatPluginMain.instance
    }

    private val server = getInstance().server

    private val config = getInstance().config

    private val administrator = requireNotNull(config.getString("administrator").toString())

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val p = e.player
        val sm = server.scoreboardManager
        val sc = sm.mainScoreboard

        if (p.uniqueId.toString() in administrator) {
            val administrator = sc.getTeam("admin")
            administrator?.addEntry(p.name)
        }
        else return

        e.joinMessage(null)
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        e.quitMessage(null)
    }

    @EventHandler
    fun onPlayerCommandPreprocess(e: PlayerCommandPreprocessEvent) {
        val p = e.player

        e.isCancelled = p.uniqueId.toString() !in administrator
    }

    @EventHandler
    fun onAsyncChat(e: AsyncChatEvent) {
        val p = e.player
        val msgComponent = e.message()
        val msg = (msgComponent as TextComponent).content()

        if (p.uniqueId.toString() in administrator) {
            server.scheduler.runTask(getInstance(), Runnable {
                server.dispatchCommand(p as CommandSender, "teammsg $msg")
            })
            e.isCancelled = true
            server.consoleSender.sendMessage(text("${p.name} issued server command: /teammsg $msg"))
        }
        else {
            e.isCancelled = true
        }
    }
}