package top.snowlands.game.business

import top.snowlands.game.model.View

/**
 * 具备攻击能力的接口
 */
interface Attackable: View {

    /**
     * 所有者
     */
    val owner:View

    /**
     * 攻击力
     */
    val attackPower:Int

    /**
     * 判断是否碰撞
     */
    fun isCollision(sufferable: Sufferable):Boolean

    /**
     * 通知是否碰撞
     */
    fun notifyAttack(sufferable: Sufferable)
}