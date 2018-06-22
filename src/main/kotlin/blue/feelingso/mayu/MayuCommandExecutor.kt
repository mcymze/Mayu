package blue.feelingso.mayu

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class MayuCommandExecutor(_mayu: Mayu) : CommandExecutor {
    private val mayu = _mayu

    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean
    {
        if (args != null && sender != null) {
            if (args.isNotEmpty()) {
                when (args[0]) {
                    "reload", "r" -> reload(sender)
                    "switch", "sw" -> switch()
                }
            }
        }

        return true
    }

    private fun reload(sender: CommandSender)
    {
        if (sender.hasPermission("khaos.reload")) {
            mayu.reloadConfig()
            sender.sendMessage("[Khaos] reloaded!")
        }
        else {
            sender.sendMessage("[Khaos] You don't have a permission to execute this command")
        }
    }

    private fun switch() {
        mayu.config.set("enable", !mayu.config.getBoolean("enable"))
        mayu.saveConfig()
    }
}