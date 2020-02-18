package top.snowlands.game.model

import org.itheima.kotlin.game.core.Painter
import top.snowlands.game.Config
import top.snowlands.game.business.*
import top.snowlands.game.enums.Direction
import kotlin.random.Random

/**
 * 敌方坦克
 *
 * 敌方坦克是可以移动的(避开阻挡物)
 * 敌方坦克是可以自动的(自己动起来)
 * 可以阻塞移动
 * 可以自动射击
 * 可以被打
 * 可以销毁
 */
class Enemy(override var x: Int, override var y: Int) :Movable,AutoMoveble,Blockable,AutoShot,Sufferable,Destroyable {

    //血量
    override var blood: Int=5
    override var currentDirection: Direction=Direction.DOWN
    override val speed: Int=8
    override val height: Int=Config.block
    override val width: Int=Config.block
    //坦克不可以走的方向
    private var badDirection:Direction?=null

    private var lastShotTime=0L
    private var shotFrequency=800L

    private var lastMoveTime=0L
    private var moveFrequency=100L

//    override fun willCollision(block: Blockable): Direction? {
//        return null
//    }

    override fun notifyCollision(direction: Direction?, block: Blockable?) {
        badDirection=direction
    }



    override fun draw() {

        //坦克方向变量
        //方式二
        val imgPath:String = when (currentDirection) {
            Direction.UP -> "img/enemy1U.gif"
            Direction.DOWN -> "img/enemy1D.gif"
            Direction.LEFT -> "img/enemy1L.gif"
            Direction.RIGHT -> "img/enemy1R.gif"
        }
        Painter.drawImage(imgPath, x, y)
    }

    override fun autoMove() {
        //频率检验
        val current=System.currentTimeMillis()
        if(current-lastMoveTime<moveFrequency){
            return
        }
        lastMoveTime=current

        if(currentDirection==badDirection){
            //这个方向不好走
            //改变自己方向
            currentDirection=rdmDirection(badDirection)
            return
        }

        //改坦克坐标
        //根据不同的方向，改变对应的坐标
        when(currentDirection){
            Direction.UP-> y-=speed
            Direction.DOWN-> y+=speed
            Direction.LEFT-> x-=speed
            Direction.RIGHT-> x+=speed
        }


        //越界判断
        if(x<0)x=0
        if(x>Config.gameWidth-width)x=Config.gameWidth-width
        if(y<0)y=0
        if(y>Config.gameHeight-height)y=Config.gameHeight-height
    }

    private fun rdmDirection(bad:Direction?):Direction{
        val i:Int= Random.nextInt(4)
        val direction: Direction = when (i) {
            0 -> Direction.UP
            1 -> Direction.DOWN
            2 -> Direction.LEFT
            3 -> Direction.RIGHT
            else -> Direction.RIGHT
        }
        //判断不是错误方向
        if(direction==bad){
            return rdmDirection(bad)//尾递归调用
        }

        return direction
    }

    override fun autoShot(): View? {
        val current=System.currentTimeMillis()
        if(current-lastShotTime<shotFrequency){
            return null
        }
        lastShotTime=current
        return Bullet(currentDirection,{bulletWidth,bulletHeight->

            val tankX:Int=x
            val tankY:Int=y
            val tankWidth:Int=width
//            val bulletHeight:Int=32//不写死，由子弹自身决定的
//            val bulletWidth:Int=8
            //计算子弹真实的坐标
            //bulletX=tankX+(tankWidth-bulletWidth)/2
            //bulletY=tankY-bulletHeight/2
            var bulletX=0
            var bulletY=0

            //如果坦克是向上的，计算子弹的位置

            when(currentDirection){
                Direction.UP->{
                    bulletX=tankX+(tankWidth-bulletWidth)/2
                    bulletY=tankY-bulletHeight/2
                }
                Direction.DOWN->{
                    bulletX=tankX+(tankWidth-bulletWidth)/2
                    bulletY=tankY+Config.block-bulletHeight/2
                }
                Direction.LEFT->{
                    bulletY=tankY+(tankWidth-bulletHeight)/2
                    bulletX=tankX-bulletWidth/2
                }
                Direction.RIGHT->{
                    bulletY=tankY+(tankWidth-bulletHeight)/2
                    bulletX=tankX+Config.block-bulletWidth/2
                }
            }

            Pair(bulletX,bulletY)
        },this)
    }
    override fun notifySuffer(attackable: Attackable): Array<View>? {
        //如果是同一方的炮弹，挨打不掉血，没反应
        if(attackable.owner is Enemy){
            return null
        }


        blood-=attackable.attackPower
        return arrayOf(Blast(x,y))
    }

    //是否可以被销毁
    override fun isDestroyable(): Boolean=blood<=0

}