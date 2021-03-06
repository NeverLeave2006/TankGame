package top.snowlands.game.business


import top.snowlands.game.Config
import top.snowlands.game.enums.Direction
import top.snowlands.game.model.View

/**
 * 移动运动的能力
 */
interface Movable:View {

    /**
     * 可移动的物体存在方向
     */
    val currentDirection:Direction

    /**
     * 可移动物体移动的速度
     */
    val speed:Int

    /**
     * 判断移动的物体是否和阻塞的物体发生碰撞
     *
     * @return 要碰撞的方向，如果为null，说明没有碰撞
     */
    fun willCollision(block: Blockable):Direction?{
        //将要碰撞时做判断
        //检测碰撞
        //未来的坐标
        var x:Int=this.x
        var y:Int=this.y
        when(currentDirection){
            Direction.UP-> y-=speed
            Direction.DOWN-> y+=speed
            Direction.LEFT-> x-=speed
            Direction.RIGHT-> x+=speed
        }

        //和边界进行检测
        //越界判断
        if(x<0)return Direction.LEFT
        if(x> Config.gameWidth-width)return Direction.RIGHT
        if(y<0)return Direction.UP
        if(y> Config.gameHeight-height)return Direction.DOWN

        val collision:Boolean=checkCollision(block.x,block.y,block.width,block.height,x,y,width,height)
        return if(collision)currentDirection else null
    }

    /**
     * 通知碰撞
     */
    fun notifyCollision(direction: Direction?,block: Blockable?)
}