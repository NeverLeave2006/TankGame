package top.snowlands.game

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import org.itheima.kotlin.game.core.Painter
import org.itheima.kotlin.game.core.Window
import top.snowlands.game.business.*
import top.snowlands.game.enums.Direction
import top.snowlands.game.model.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.ArrayList
import java.util.concurrent.CopyOnWriteArrayList

class GameWindow:Window(title = "坦克大战1.0"
    ,icon = "img/p1tankR.gif"
    ,width = Config.gameWidth
    ,height = Config.gameHeight){

    //管理元素的集合
    //选择线程安全的集合
    private val views=CopyOnWriteArrayList<View>()

    //晚点创建
    private lateinit var tank:Tank

    //游戏是否结束
    private var gameOver:Boolean=false

    //敌方坦克的数量
    private var enemyTotalSize:Int=20
    //敌方最多显示的坦克的数量
    private var enemyActivelSize:Int=6
    //敌方的出生点
    private val enemyBornLocation: ArrayList<Pair<Int, Int>> =arrayListOf<Pair<Int,Int>>()
    //出生地点下标
    private var bornIndex=0

    override fun onCreate() {

        //地图
        //通过读文件的方式创建地图
        val file=File(javaClass.getResource("/map/1.map").path)
        val resourceAsStream: InputStream = javaClass.getResourceAsStream("/map/1.map")
        val reader: BufferedReader = BufferedReader(InputStreamReader(resourceAsStream, "UTF-8"))
        //读取文件代码中的行
        val lines:List<String> = reader.readLines()
        //循环遍历
        var lineNum=0
        lines.forEach {line->
            var columnNum=0
            line.toCharArray().forEach {colum->
                when(colum){
                    '砖'->views.add(Wall(columnNum*Config.block,lineNum*Config.block))
                    '铁'->views.add(Steel(columnNum*Config.block,lineNum*Config.block))
                    '水'->views.add(Water(columnNum*Config.block,lineNum*Config.block))
                    '草'->views.add(Grass(columnNum*Config.block,lineNum*Config.block))
                    '敌'->enemyBornLocation.add(Pair(columnNum*Config.block,lineNum*Config.block))
                }
                columnNum++
            }
            lineNum++
        }

        //添加我方的坦克
        tank = Tank(Config.block * 10, Config.block * 12)
        views.add(tank)

        ///添加大本营
        views.add(Camp(Config.gameWidth/2-Config.block,Config.gameHeight-96))
    }

    override fun onDisplay() {
        //绘制图像

        //绘制地图中的元素,包括坦克
        views.forEach {
            it.draw()
        }
        println("${views.size}")
    }

    override fun onKeyPressed(event: KeyEvent) {
        //用户操作时
        if(!gameOver){
            when(event.code){
                KeyCode.W->{
                    tank.move(Direction.UP)
                }
                KeyCode.A->{
                    tank.move(Direction.LEFT)
                }
                KeyCode.S->{
                    tank.move(Direction.DOWN)
                }
                KeyCode.D->{
                    tank.move(Direction.RIGHT)
                }
                KeyCode.SPACE->{
                    //发射子弹
                    val bullet:Bullet=tank.shot()
                    //交给views管理
                    views.add(bullet)
                }

            }
        }

    }

    override fun onRefresh() {
        //业务逻辑

        //检测销毁，在游戏开始钱跑一遍
        views.filter { it is Destroyable }.forEach {
            if((it as Destroyable).isDestroyable()){
                views.remove(it)
                if(it is Enemy){
                    enemyTotalSize--
                }
                val destroy: Array<View>? = it.showDestroy()
                destroy?.let {
                    views.addAll(destroy)
                }
            }
        }

        //判断游戏是否结束
        if(gameOver)return

        //判断运动物体和阻塞物体是否发生碰撞，
        //1.找到运动物体
        views.filter { it is Movable }.forEach  {move->

            move as Movable

            var badDirection:Direction?=null
            var badBlockable:Blockable?=null
            //2.找到阻塞的物体
            //不要和自己比较
            views.filter { (it is Blockable) and (move!=it) }.forEach blockTag@{block->
                //3.遍历集合，找到是否发生碰撞
                //move和block是否碰撞

                block as Blockable
                //获得碰撞方向
                val direction:Direction? = move.willCollision(block)
                direction?.let {
                    //移动的发现碰撞，跳出当前循环
                    badDirection=direction
                    badBlockable=block

                    return@blockTag
                }
            }
            //找到和move碰撞的阻塞块block,找到会碰撞的方向
            //通知可以移动的物体，会在哪个方向和哪个物体碰撞
            move.notifyCollision(badDirection,badBlockable)
        }

        //检测自动移动能力的物体,让他们自己动起来
        views.filter { it is AutoMoveble }.forEach {
            (it as AutoMoveble).autoMove()
        }



        //检测自动射击
        views.filter { it is AutoShot }.forEach {
            it as AutoShot
            val shot = it.autoShot()
            shot?.let {
                views.add(shot)
            }
        }

        //检测具备攻击能力和被攻击能力的物体间是否产生碰撞
        //1)过滤具备攻击能力的
        views.filter { it is Attackable }.forEach {attack->
            attack as Attackable
            //2）过滤受攻击能力的
            //攻击方的源不可以是发射方
            //攻击方也不可打自己
            views.filter { (it is Sufferable)and(attack.owner!=it)and(attack!=it) }.forEach sufferTag@{suffer->
                suffer as Sufferable
                //3) 判断是否碰撞
                if(attack.isCollision(suffer)){//如果产生碰撞
                    //通知攻击者产生碰撞
                    attack.notifyAttack(suffer)
                    //通知被攻击者产生碰撞
                    val sufferView=suffer.notifySuffer(attack)
                    sufferView?.let {
                        //显示挨打的效果
                        views.addAll(sufferView)
                    }
                    return@sufferTag
                }
            }
        }

        //检测游戏是否结束
        //我方老家或者敌人老家没了
        if((views.filter { it is Camp }.isEmpty()) or (enemyTotalSize<=0)){
            gameOver=true
        }

        //检测敌方出生
        //判断当前页面上敌方的数量，小于激活量
        if((enemyTotalSize>0) and (views.filter { it is Enemy }.size<enemyActivelSize)){
            val index:Int=bornIndex%enemyBornLocation.size
            val pair: Pair<Int, Int> = enemyBornLocation[index]
            views.add(Enemy(pair.first,pair.second))
            bornIndex++
        }

    }

}