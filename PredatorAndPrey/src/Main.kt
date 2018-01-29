import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JFrame
import javax.swing.JPanel

class Main : JPanel(), Runnable {
    private val WINDOW_HEIGHT = 720
    private val WINDOW_WIDTH = 1280
    
    private lateinit var thread: Thread
    private var running = false
    
    private var window: JFrame = JFrame()
    private var screen = this as JPanel
    
    private var ticks = 0
    
    private val world: World
    private val CELL_SIZE = 3
    
    init {
        window.title = "Predator and Prey"
        window.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT)
        window.minimumSize = window.size
        window.isResizable = false
        window.setLocationRelativeTo(null)
        window.isVisible = true
        
        window.add(screen)
        screen.background = Color.black
        window.pack()
        
        world = World(screen.width / CELL_SIZE, screen.height / CELL_SIZE)
        
    }
    
    @Synchronized
    fun start() {
        thread = Thread(this)
        thread.start()
        running = true
    }
    
    @Synchronized
    private fun stop() {
        try {
            thread.join()
            running = false
        } catch(e: Exception) {
            e.printStackTrace()
        }
        
    }
    
    override fun run() {
        this.requestFocus()
        var lastTime = System.nanoTime()
        val amountOfTicks = 120.0
        val ns = 1000000000 / amountOfTicks
        var delta = 0.0
        var timer = System.currentTimeMillis()
        var frames = 0
        while(running) {
            val now = System.nanoTime()
            delta += (now - lastTime) / ns
            lastTime = now
            while(delta >= 1) {
                tick()
                delta--
            }
            if(running)
                screen.repaint()
            frames++
            
            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000
                println("FPS: " + frames)
                frames = 0
            }
        }
        stop()
    }
    
    // Runs 60 ticks per second
    private fun tick() {
        ticks++
        // Update the map (step) each X ticks
        if(ticks == 1) {
            ticks = 0
            world.step()
        }
    }
    
    override fun paint(g: Graphics) {
        super.paint(g)
        val g2d = g.create() as Graphics2D
        g2d.scale(1.0, -1.0)
        g2d.translate(0, -height)
        
        for(y in 0 until world.height) {
            for(x in 0 until world.width) {
                g2d.color = world.creatures[x][y].getColor()
                if(CELL_SIZE == 1)
                    g2d.drawPixel(x, y)
                else
                    g2d.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE)
            }
        }
        
        g2d.dispose()
    }
    
}

fun main(args: Array<String>) {
    Main().start()
}