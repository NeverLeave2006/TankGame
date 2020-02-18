package top.snowlands.game.business

import top.snowlands.game.model.View

/**
 * 遭受攻击的接口
 */
interface Sufferable: View {

    /**
     * 通知遭受攻击
     */
    fun notifySuffer(attackable: Attackable):Array<View>?

    /**
     * 血量, 生命值
     */
    val blood:Int
}