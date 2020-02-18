package top.snowlands.game.model

import org.itheima.kotlin.game.core.Painter
import top.snowlands.game.Config
import top.snowlands.game.business.Blockable

/**
 * 水墙
 * 阻塞块
 */
class Water(override var x: Int, override var y: Int):Blockable {
    //    override val x: Int=400
//    override val y: Int=400
    override val width: Int=Config.block
    override val height: Int=Config.block

    override fun draw() {
        Painter.drawImage("img/tank/water.gif", x, y)
    }

}