package top.snowlands.game.ext

import top.snowlands.game.model.View

fun View.checkCollision(view: View):Boolean{
    val checkCollision = checkCollision(x, y, width, height, view.x, view.y, view.width, view.height)
    return checkCollision
}