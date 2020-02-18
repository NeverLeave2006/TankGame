package top.snowlands.game.business

import top.snowlands.game.enums.Direction
import top.snowlands.game.model.View

/**
 * 自动移动的能力
 */
interface AutoMoveble:View {

    /**
     * 方向
     */
    val currentDirection:Direction
    /**
     * 速度
     */
    val speed:Int
    fun autoMove()
}