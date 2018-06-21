package blue.feelingso.mayu

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

class Mayu : JavaPlugin() {

    override fun onEnable() {
        logger.info("hello")
        saveDefaultConfig()
        server.pluginManager.registerEvents(MayuListener(this), this)
    }

    override fun onDisable() {
    }

    public fun reload() {
        reloadConfig()
    }

    public fun getConf() : FileConfiguration {
        return config
    }
}
