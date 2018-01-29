import java.awt.Color

class Creature(var type: Type) {
    
    companion object {
        const val MAX_HEALTH = 100
    }
    
    var hp: Int = 10
        set(value) {
            field = Math.min(value, MAX_HEALTH)
        }
    
    fun getColor(): Color {
        val factor = hp.toFloat() / MAX_HEALTH.toFloat()
        return type.color.scale(factor)
    }
    
    enum class Type(val color: Color) {
        NONE(Color.BLACK),
        Predator(Color.RED),
        Prey(Color.GREEN)
    }
    
}