import java.awt.Color
import java.awt.Font
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
    private val CELL_SIZE = 4
    
    private val customFont: Font
    
    init {
        val stream = Main::class.java.getResourceAsStream("Resources/small_pixel-7.ttf")
        customFont = Font.createFont(Font.TRUETYPE_FONT, stream)
        
        window.title = "Predator and Prey"
        window.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT)
        window.minimumSize = window.size
        window.isResizable = false
        window.setLocationRelativeTo(null)
        
        window.add(screen)
        screen.background = Color.black
        window.pack()
        
        world = World(screen.width / CELL_SIZE, screen.height / CELL_SIZE)
    
        window.isVisible = true
        
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
        
        if(world == null) return
        
        for(y in 0 until world.height) {
            for(x in 0 until world.width) {
                // NOTE: No need to display the black squares
                if(world.creatures[x][y].type == Creature.Type.NONE)
                    continue
                
                g2d.color = world.creatures[x][y].getColor()
                g2d.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE)
            }
        }
        
        g2d.scale(1.0, -1.0)
        val prey = world.preyCount
        val predator = world.predatorCount
        val steps = world.stepCount
        g2d.color = Color.WHITE
        g2d.font = customFont.deriveFont(screen.width / 50f)
        g2d.drawString("Prey: $prey", 10, -height + 25)
        g2d.drawString("Predator: $predator", 10, -height + 50)
        g2d.drawString("Steps: $steps", 10, -height + 75)
        
        g2d.dispose()
    }
    
}

fun main(args: Array<String>) {
    Main().start()
}