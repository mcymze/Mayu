package blue.feelingso.mayu

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

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
        if (!player.hasPermission("mayu.mine")) return

        // スニーク時無効
        if (player.isSneaking) return

        // ブロックとツールが対象かチェック
        if (!mayu.getConf().getStringList("allowTools.${tool.type.toString()}").contains(block.type.toString())) return

        // 掘り開始
        val count = mineRecursively(block, tool)

        if (mayu.getConf().getBoolean("consume", false)) tool.durability = (tool.durability + count).toShort()

        if (tool.type.maxDurability < tool.durability) player.inventory.remove(tool)
    }

    private fun mineRecursively(block :Block, tool :ItemStack, cnt :Int = 20) : Int {
        if (cnt < 0) return 0
        val type = block.type.toString()
        var count = 0
        breakBlock(block, tool)
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1)
                {
                    if (x == 0 && y == 0 && z == 0) break
                    block.getRelative(x, y, z).let {
                        if (type == it.type.toString() && !(block.x == it.x && block.y == it.y && block.z == it.z))
                            count += mineRecursively(it, tool, cnt - 1)
                    }
                }
            }
        }

        return count + 1
    }

    private fun breakBlock(block :Block, tool :ItemStack) {
        // シルクタッチつき
        if (tool.enchantments.containsKey(Enchantment.SILK_TOUCH))
        {
            block.world.dropItem(block.location, ItemStack(block.type))
            block.breakNaturally(ItemStack(Material.AIR))

            // 同じブロックを落とす
        }
        else
        {
            // fortuneの場合複数回る．．．と思っていたのですが．．．
            for (drop in block.getDrops(tool))
            {
                block.world.dropItem(block.location, drop)
            }
            block.breakNaturally(ItemStack(Material.AIR))
        }
    }
}