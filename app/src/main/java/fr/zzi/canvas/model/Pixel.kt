package fr.zzi.canvas.model

data class Pixel(
    val x: Int = -1,
    val y: Int = -1,
    val color: PixelColor = PixelColor.WHITE
) {
    var id: String? = null
}