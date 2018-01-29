import java.awt.Color
import java.awt.Graphics2D

/**
 * Draw a single pixel with [Graphics2D]
 *
 * @param x The x position of the pixel
 * @param y The y position of the pixel
 */
fun Graphics2D.drawPixel(x: Int, y: Int) {
    this.drawLine(x, y, x, y)
}

/**
 * Scale a Color by a specific factor and return the new Color
 *
 * @param factor The factor to scale the color
 *
 * @return The new Color R,G and B clamped between 0 and 255
 */
fun Color.scale(factor: Float): Color {
    val red = (this.red * factor).clamp(0f, 255f) / 255
    val green = (this.green * factor).clamp(0f, 255f) / 255
    val blue = (this.blue * factor).clamp(0f, 255f) / 255
    return Color(red, green, blue)
}

/**
 * Clamp a Float between a [min] and a [max] value
 *
 * @param min The min value of the new Float
 * @param max The max value of the new Float
 *
 * @return The new Float between [min] and [max] included
 */
fun Float.clamp(min: Float, max: Float): Float {
    return Math.max(Math.min(this, max), min)
}