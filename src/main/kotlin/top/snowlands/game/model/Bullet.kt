package top.snowlands.game.model

import org.itheima.kotlin.game.core.Painter
import top.snowlands.game.Config
import top.snowlands.game.business.Attackable
import top.snowlands.game.business.AutoMoveble
import top.snowlands.game.business.Destroyable
import top.snowlands.game.business.Sufferable
import top.snowlands.game.enums.Direction
import top.snowlands.game.ext.checkCollision


/**
 * 子弹
 * create()函数返回两个值，是x,y
 */
class Bullet(override val currentDirection:Direction, create:(width:Int, height:Int)->Pair<Int,Int>,
             override val owner: View
) :AutoMoveble,Destroyable,Attackable,Sufferable {

    //给子弹一个方向，方向由坦克来决定的



    override val width: Int
    override val height: Int
    override val speed: Int=12


    override val blood: Int=1
    override var x: Int = 0
    override var y: Int = 0
    override val attackPower: Int=1

    private var isDestroyable=false

    override fun isDestroyable(): Boolean {
        //如果销毁属性即销毁
        if(isDestroyable)return true
        //子弹在脱离了屏幕后需要被销毁
        if(x<-width)return true
        if(x>Config.gameWidth+width)return true
        if(y<-height)return true
        if(y>Config.gameHeight)return true
        return false
    }


    override fun autoMove() {
        //根据自己的方向改变x和y
        when(currentDirection){
            Direction.UP->y-=speed
            Direction.DOWN->y+=speed
            Direction.LEFT->x-=speed
            Direction.RIGHT->x+=speed
        }
    }



    private var imagePath:String

    init{
        //先计算宽度和高度
        imagePath = when (currentDirection) {
            Direction.UP -> "img/bullet_up.png"
            Direction.DOWN -> "img/bullet_down.png"
            Direction.LEFT -> "img/bullet_left.png"
            Direction.RIGHT -> "img/bullet_right.png"
        }
        val size:Array<Int> = Painter.size(imagePath)
        width=size[0]
        height=size[1]
        val pair:Pair<Int,Int> =create.invoke(width,height)
        x=pair.first
        y=pair.second
    }

    override fun draw() {
//        val imagePath = when (direction) {
//            Direction.UP -> "img/bullet_up.png"
//            Direction.DOWN -> "img/bullet_down.png"
//            Direction.LEFT -> "img/bullet_left.png"
//            Direction.RIGHT -> "img/bullet_right.png"
//        }
        Painter.drawImage(imagePath,x,y)
    }



    /**
     * 判断是否产生碰撞
     */
    override fun isCollision(sufferable: Sufferable): Boolean {
        return checkCollision(sufferable)
    }

    /**
     * 接受攻击通知
     */
    override fun notifyAttack(sufferable: Sufferable) {
//        println("子弹接受到碰撞...")
        //子弹打到墙上后，子弹要销毁掉
        //通知销毁
        isDestroyable=true

    }

    override fun notifySuffer(attackable: Attackable): Array<View>? {
        return arrayOf(Blast(x,y))
    }
}
