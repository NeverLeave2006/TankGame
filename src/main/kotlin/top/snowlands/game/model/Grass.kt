package top.snowlands.game.model

import org.itheima.kotlin.game.core.Painter
import top.snowlands.game.Config

/**
 * 草坪
 */
class Grass(override var x: Int, override var y: Int):View {
    //位置
//    override val x: Int=200
//    override val y: Int=200
    //宽高
    override val width: Int=Config.block
    override val height: Int=Config.block
    //显示行为
    override fun draw() {
        Painter.drawImage("img/tank/grass.png", x, y)
    }

//    //位置
//    var x:Int=200
//    var y:Int=200
//    //宽高
//    var width:Int= Config.block
//    var height:Int= Config.block
//    //显示行为
//    fun draw() {
//        Painter.drawImage("img/tank/grass.png", x, y)
//    }
}