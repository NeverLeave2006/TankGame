package top.snowlands.game.model

import org.itheima.kotlin.game.core.Painter
import top.snowlands.game.Config
import top.snowlands.game.business.Attackable
import top.snowlands.game.business.Blockable
import top.snowlands.game.business.Destroyable
import top.snowlands.game.business.Sufferable

/**
 * 大本营
 * 具备阻挡的功能
 * 具备受攻击的能力
 * 具备被销毁的能力
 */
class Camp(override var x: Int, override var y: Int) :Blockable,Sufferable,Destroyable {


    //血量
    override var blood: Int=12
    override var height: Int=Config.block+32
    override var width: Int=(Config.block*2)
    override fun draw() {

        //血量低于6时画的是砖墙
        //血量低于3时，没有墙
        if(blood<=3){
            width=Config.block
            height=Config.block
            x=(Config.gameWidth-Config.block)/2
            y=Config.gameHeight-Config.block
            Painter.drawImage("img/camp.jpg",x,y)
        }else if(blood<=6){
            //绘制外围的铁砖块
            Painter.drawImage("img/obstacle/wall.gif",x,y)
            Painter.drawImage("img/obstacle/wall.gif",x+32,y)
            Painter.drawImage("img/obstacle/wall.gif",x+64,y)
            Painter.drawImage("img/obstacle/wall.gif",x+96,y)
            Painter.drawImage("img/obstacle/wall.gif",x,y+32)
            Painter.drawImage("img/obstacle/wall.gif",x,y+64)
            Painter.drawImage("img/obstacle/wall.gif",x+96,y+32)
            Painter.drawImage("img/obstacle/wall.gif",x+96,y+64)
            Painter.drawImage("img/camp.jpg",x+32,y+32)
        }else{
            //绘制外围的铁砖块
            Painter.drawImage("img/obstacle/tugai.net.20101117134209.gif",x,y)
            Painter.drawImage("img/obstacle/tugai.net.20101117134209.gif",x+32,y)
            Painter.drawImage("img/obstacle/tugai.net.20101117134209.gif",x+64,y)
            Painter.drawImage("img/obstacle/tugai.net.20101117134209.gif",x+96,y)
            Painter.drawImage("img/obstacle/tugai.net.20101117134209.gif",x,y+32)
            Painter.drawImage("img/obstacle/tugai.net.20101117134209.gif",x,y+64)
            Painter.drawImage("img/obstacle/tugai.net.20101117134209.gif",x+96,y+32)
            Painter.drawImage("img/obstacle/tugai.net.20101117134209.gif",x+96,y+64)
            Painter.drawImage("img/camp.jpg",x+32,y+32)
        }


    }

    override fun notifySuffer(attackable: Attackable): Array<View>? {
        //被打时
        blood-=attackable.attackPower

        if(blood==3||blood==6){
            return arrayOf(Blast(x-16,y-16)
                ,Blast(x+16,y-16)
                ,Blast(x+Config.block-16,y-16)
                ,Blast(x+Config.block+16,y-16)
                ,Blast(x+Config.block+48,y-16)
                ,Blast(x-16,y+16)
                ,Blast(x-16,y+Config.block-16)
                ,Blast(x-16,y+Config.block+16)
                ,Blast(x+Config.block*2-16,y+16)
                ,Blast(x+Config.block*2-16,y+Config.block-16)
                ,Blast(x+Config.block*2-16,y+Config.block+16)
            )
        }

        return null
    }

    override fun isDestroyable(): Boolean =blood<=0
    override fun showDestroy(): Array<View>? {
        return arrayOf(Blast(x-32,y-32)
            ,Blast(x,y-32)
            ,Blast(x+32,y-32)
            ,Blast(x-32,y)
            ,Blast(x,y)
            ,Blast(x+32,y)
            ,Blast(x-32,y+32)
            ,Blast(x,y+32)
            ,Blast(x+32,y+32)
        )
    }
}