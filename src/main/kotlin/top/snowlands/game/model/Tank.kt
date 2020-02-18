package top.snowlands.game.model

import org.itheima.kotlin.game.core.Painter
import top.snowlands.game.Config
import top.snowlands.game.business.Attackable
import top.snowlands.game.business.Blockable
import top.snowlands.game.business.Movable
import top.snowlands.game.business.Sufferable
import top.snowlands.game.enums.Direction

/**
 * 我方坦克
 * 具备移动能力
 * 具备阻挡能力
 */
class Tank(override var x: Int, override var y: Int) :Movable,Blockable,Sufferable {

    //血量
    override var blood: Int=20
    override val width: Int= Config.block
    override val height: Int=Config.block

    //方向
    override var currentDirection:Direction=Direction.UP//方向默认向上
    //速度
    override val speed:Int=8//每次8个像素

    //坦克不可以走的方向
    private var badDirection:Direction?=null

    //子弹发射频率限制
    private var lastShotTime=0L
    private var shotFrequency=800L



    override fun draw() {
        //根据坦克的方向进行绘制
        //方式一
//        when(currentDirection){
//            Direction.UP-> Painter.drawImage("img/p1tankU.gif", x, y)
//            Direction.DOWN-> Painter.drawImage("img/p1tankD.gif", x, y)
//            Direction.LEFT-> Painter.drawImage("img/p1tankL.gif", x, y)
//            Direction.RIGHT-> Painter.drawImage("img/p1tankR.gif", x, y)
//        }

        //坦克方向变量
        //方式二
        val imgPath:String = when (currentDirection) {
            Direction.UP -> "img/p1tankU.gif"
            Direction.DOWN -> "img/p1tankD.gif"
            Direction.LEFT -> "img/p1tankL.gif"
            Direction.RIGHT -> "img/p1tankR.gif"
        }
        Painter.drawImage(imgPath, x, y)
    }

    /**
     * 坦克移动
     */
    fun move(direction: Direction){
        //判断是否是往要碰撞的方向走
        if(direction==badDirection){
            //不往下执行，不走了
            return
        }

        //当前的方向和期望移动的方向不一致时，只做方向改变
        if(this.currentDirection!=direction){
            //改方向
            this.currentDirection=direction
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

//    override fun willCollision(block: Blockable): Direction? {
//
//        //将要碰撞时做判断
//        //检测碰撞
//        //未来的坐标
//        var x:Int=this.x
//        var y:Int=this.y
//        when(currentDirection){
//            Direction.UP-> y-=speed
//            Direction.DOWN-> y+=speed
//            Direction.LEFT-> x-=speed
//            Direction.RIGHT-> x+=speed
//        }
//
////        //检测下一步是否碰撞
////
////        //kotlin闭包写法
////        val collision:Boolean= when {
////            block.y+block.height<=y -> {
////                //如果阻挡物在运动物上方,不碰撞
////                false;
////            }
////            y+height<=block.y -> {
////                //如果阻挡物在运动物下方,不碰撞
////                false;
////            }
////            block.x+block.width<=x->{
////                false
////            }
////            else -> x+width > block.x
////        }
//        val collision:Boolean=checkCollision(block.x,block.y,width,height,x,y,width,height)
//        return if(collision)currentDirection else null
//    }

    override fun notifyCollision(direction: Direction?, block: Blockable?) {
        //TODO:接受到碰撞信息
        this.badDirection=direction
    }


    /**
     * 发射子弹的方法
     */
    fun shot():Bullet {
//        val current=System.currentTimeMillis()
//        if(current-lastShotTime<shotFrequency){
//            return
//        }
//        lastShotTime=current


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
        blood-=attackable.attackPower
        return arrayOf(Blast(x,y))
    }


}