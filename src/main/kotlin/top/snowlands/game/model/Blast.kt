package top.snowlands.game.model

import org.itheima.kotlin.game.core.Painter
import top.snowlands.game.Config
import top.snowlands.game.business.Destroyable

/**
 * 爆炸物
 */
class Blast(override var x: Int, override var y: Int) :Destroyable {
    override val width: Int= Config.block
    override val height: Int=Config.block
    private val imagePaths= arrayListOf<String>()
    private var index:Int=0

    init {
        (1..32).forEach {
            imagePaths.add("img/blast_${it}(已去底).png")
        }
    }

    override fun draw() {
        val i:Int=index%imagePaths.size
        Painter.drawImage(imagePaths[i],x,y)
        index++
    }

    override fun isDestroyable(): Boolean =index>=imagePaths.size

}