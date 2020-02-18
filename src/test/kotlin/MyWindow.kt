import javafx.application.Application
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.paint.Color
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter
import org.itheima.kotlin.game.core.Window

class MyWindow: Window() {
    override fun onCreate() {
        println("窗体创建")
    }

    override fun onDisplay() {
        //println("onDisplay...")
        //窗体渲染回调，不停执行
        //绘制图片
        Painter.drawImage("img/p1tankU.gif",200,200)

        //绘制颜色
        Painter.drawColor(Color.WHITE,20,20,100,100)

        //写字
        Painter.drawText("你好",30,30)

    }

    override fun onKeyPressed(event: KeyEvent) {
        //按键响应
        when(event.code){
            KeyCode.ENTER-> println("点击了enter按钮")
            KeyCode.SPACE->Composer.play("img/audio/fire.wav")
        }

    }

    override fun onRefresh() {
        //刷新，做业务逻辑，做耗时的操作

    }
}
fun main(){
    Application.launch(MyWindow::class.java)
}