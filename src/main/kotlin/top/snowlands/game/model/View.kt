package top.snowlands.game.model

/**
 * 显示的视图，定义显示规范
 */
interface View {

    //可以定义属性，让实现类实现
    //位置
    var x:Int
    var y:Int
    //高度
    val width:Int
    val height:Int
    //显示
    fun draw()

    fun checkCollision(x1:Int,y1:Int,w1:Int,h1:Int,x2:Int,y2:Int,w2:Int,h2:Int):Boolean{
        //两个物体x,y,w,h的比较
        //kotlin闭包写法
        return when {
            y2+h2<=y1 -> {
                //如果阻挡物在运动物上方,不碰撞
                false;
            }
            y1+h1<=y2 -> {
                //如果阻挡物在运动物下方,不碰撞
                false;
            }
            x2+w2<=x1->{
                false
            }
            else -> x1+w1 > x2
        }
    }


}