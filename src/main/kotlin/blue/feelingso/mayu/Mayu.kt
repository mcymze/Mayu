package blue.feelingso.mayu

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

class Mayu : JavaPlugin() {

    override fun onEnable() {
        logger.info("hello")
        saveDefaultConfig()
        server.pluginManager.registerEvents(MayuListener(this), this)
        getCommand("mayu").executor = MayuCommandExecutor(this)
    }

    override fun onDisable() {
    }

    fun reload() {
        reloadConfig()
    }

    fun getConf() : FileConfiguration {
        return config
    }

    fun log(str :String) {
        logger.info(str)
    }
}
