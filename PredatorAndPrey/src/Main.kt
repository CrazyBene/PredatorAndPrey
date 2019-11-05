class Main : Runnable {
    private val WINDOW_HEIGHT = 720
    private val WINDOW_WIDTH = 1280
    
    private lateinit var thread: Thread
    private var running = false

    private val window: WorldView

    private var ticks = 0
    
    private val world: World
    private val CELL_SIZE = 4

    
    init {
        window = WorldView(CELL_SIZE, WINDOW_WIDTH, WINDOW_HEIGHT)
        world = World(window.screen.width / CELL_SIZE, window.screen.height / CELL_SIZE)
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
        window.requestFocus()
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
                window.update(world)
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
}

fun main(args: Array<String>) {
    Main().start()
}