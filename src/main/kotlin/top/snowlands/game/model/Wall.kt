package top.snowlands.game.model

import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter
import top.snowlands.game.Config
import top.snowlands.game.business.Attackable
import top.snowlands.game.business.Blockable
import top.snowlands.game.business.Destroyable
import top.snowlands.game.business.Sufferable

/**
 * 砖墙
 * 具备阻塞能力
 * 具备挨打能力
 * 具备销毁能力
 */
class Wall(override var x: Int, override var y: Int):Blockable,Sufferable,Destroyable {

    override var blood: Int=3

    //位置
//    override val x: Int=100
//    override val y: Int=100
    //宽高
    override val width: Int=Config.block
    override val height: Int=Config.block
    //显示行为
    override fun draw() {
        Painter.drawImage("img/walls.gif",x,y)
    }
//    //位置
//    var x:Int=100
//    var y:Int=100
//    //宽高
//    var width:Int=Config.block
//    var height:Int=Config.block
//    //显示行为
//    fun draw(){
//        Painter.drawImage("img/walls.gif",x,y)
//    }

    override fun notifySuffer(attackable: Attackable): Array<View>? {
//        println("打到砖墙了...")
        //砖墙要被销毁,砖墙掉血
        blood-=attackable.attackPower;
        //发出声音
        Composer.play("img/audio/hit.wav")
        return arrayOf(Blast(x,y))
    }

    override fun isDestroyable(): Boolean =  blood<=0

}