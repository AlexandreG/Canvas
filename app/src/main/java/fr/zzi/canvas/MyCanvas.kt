package fr.zzi.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class MyCanvas @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val PIXEL_NUMBER = 4
    }

    private var data: List<List<Int>> =
        listOf(listOf(1, 1, 1, 0), listOf(0, 0, 0, 1), listOf(0, 1, 0, 1), listOf(1, 0, 0, 1))
    private val paint = Paint()
    private var pixelSize = 0

    init {
        paint.color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawData(canvas)
    }

    private fun drawData(canvas: Canvas?) {
        for (i in 0..(data.size - 1)) {
            for (j in 0..(data.size - 1)) {
                if (data[i][j] == 1) {
                    drawPixel(canvas, i.toFloat(), j.toFloat())
                }
            }
        }
    }

    private fun drawPixel(canvas: Canvas?, i: Float, j: Float) {
        canvas?.drawRect(i * pixelSize, j * pixelSize, (i + 1) * pixelSize, (j + 1) * pixelSize, paint)
    }

    fun refresh(newData: List<List<Int>>) {
        data = newData
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        pixelSize = MeasureSpec.getSize(widthMeasureSpec) / PIXEL_NUMBER
    }
}