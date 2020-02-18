package top.snowlands.game.model

import org.itheima.kotlin.game.core.Painter
import top.snowlands.game.Config
import top.snowlands.game.business.Attackable
import top.snowlands.game.business.Blockable
import top.snowlands.game.business.Sufferable

/**
 * 铁墙
 * 具有阻塞能力
 * 具有接受攻击的能力
 */
class Steel(override var x: Int, override var y: Int):Blockable,Sufferable {


    override val blood: Int=1
    //    override val x: Int=300
//    override val y: Int=300
    override val width: Int= Config.block
    override val height: Int=Config.block

    override fun draw() {
        Painter.drawImage("img/steels.gif", x, y)
    }

    override fun notifySuffer(attackable: Attackable): Array<View>? {
        return null
    }
}