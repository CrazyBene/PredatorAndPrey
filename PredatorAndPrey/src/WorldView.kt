import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JFrame
import javax.swing.JPanel

class WorldView(private val CELL_SIZE: Int, WINDOW_WIDTH: Int, WINDOW_HEIGHT: Int) : JPanel() {
    private val customFont: Font

    private var window: JFrame = JFrame()
    public var screen = this as JPanel

    private var world: World? = null

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
        window.isVisible = true
    }

    fun update(world: World) {
        this.world = world
        this.repaint()
    }

    override fun paint(g: Graphics) {
        super.paint(g)
        val g2d = g.create() as Graphics2D
        g2d.scale(1.0, -1.0)
        g2d.translate(0, -height)

        if(world == null) return

        for(y in 0 until world!!.height) {
            for(x in 0 until world!!.width) {
                if(world!!.creatures[x][y].type == Creature.Type.NONE)
                    continue

                g2d.color = world!!.creatures[x][y].getColor()
                g2d.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE)
            }
        }

        g2d.scale(1.0, -1.0)
        val prey = world!!.preyCount
        val predator = world!!.predatorCount
        val steps = world!!.stepCount
        g2d.color = Color.WHITE
        g2d.font = customFont.deriveFont(screen.width / 50f)
        g2d.drawString("Prey: $prey", 10, -height + 25)
        g2d.drawString("Predator: $predator", 10, -height + 50)
        g2d.drawString("Steps: $steps", 10, -height + 75)

        g2d.dispose()
    }

}