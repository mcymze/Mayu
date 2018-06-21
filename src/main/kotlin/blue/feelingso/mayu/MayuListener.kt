package blue.feelingso.mayu

import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class MayuListener(_mayu :Mayu) : Listener {
    private val mayu = _mayu

    @EventHandler
    fun onBlockBroken(ev : BlockBreakEvent) {
        val player = ev.player
        val block = ev.block
        val tool = player.inventory.itemInMainHand

        // 機能の有効かをチェック
        if (!mayu.getConf().getBoolean("enable", true)) return

        // プレイヤの権限をチェック
        if (!ev.player.hasPermission("mayu.mine")) return

        // ブロックとツールが対象かチェック
        if (!mayu.getConf().getStringList("allowTools.${tool.type.toString()}").contains(block.type.toString())) return

        ev.player.sendMessage(block.type.toString())

        // 掘り開始
        val count = mineRecursively(block)

        if (mayu.getConf().getBoolean("consume", false)) tool.durability = (tool.durability + count).toShort()

        if (tool.type.maxDurability < tool.durability) player.inventory.remove(tool)

        ev.player.sendMessage(count.toString())
    }

    private fun mineRecursively(block :Block, cnt :Int = 6) : Int {
        if (cnt < 0) return 0
        var count = 0
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    val _block = block.getRelative(x, y, z)
                    if (block.type === _block.type && block.hashCode() != _block.hashCode()) {
                        count += mineRecursively(_block, cnt - 1)
                    }
                }
            }
        }
        block.breakNaturally()
        return count + 1
    }
}