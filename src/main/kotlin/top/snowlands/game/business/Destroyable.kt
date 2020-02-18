package top.snowlands.game.business

import top.snowlands.game.model.View

/**
 * 销毁的能力
 */
interface Destroyable:View {
    /**
     * 判断是否被销毁了
     */
    fun isDestroyable():Boolean

    /**
     * 死后自爆
     */
    fun showDestroy():Array<View>?{
        return null
    }
}