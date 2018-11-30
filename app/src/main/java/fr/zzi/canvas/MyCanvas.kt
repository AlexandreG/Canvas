package fr.zzi.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import fr.zzi.canvas.model.Board
import fr.zzi.canvas.model.Pixel
import fr.zzi.canvas.model.PixelColor

class MyCanvas @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), View.OnTouchListener {

    interface Callback {
        fun onUpdate(data: List<Pixel>)
    }

    private var pixelList: List<Pixel> = listOf()
    private lateinit var board: Board
    lateinit var callback: Callback
    private val paint = Paint()
    private var pixelSize: Int = -1

    init {
        paint.color = Color.BLACK
        setOnTouchListener(this)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawData(canvas)
    }

    private fun drawData(canvas: Canvas?) {
        pixelList
            .filter { it.color == PixelColor.BLACK }
            .apply { drawPixel(canvas, x, y) }
    }

    private fun drawPixel(canvas: Canvas?, i: Float, j: Float) {
        canvas?.drawRect(i * pixelSize, j * pixelSize, (i + 1) * pixelSize, (j + 1) * pixelSize, paint)
    }

    fun refresh(newData: List<Pixel>) {
        pixelList = newData
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        pixelSize = MeasureSpec.getSize(widthMeasureSpec) / board.pixelWidthNb
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN
        ) {
            handleTouchEvent(event.x, event.y)

            return true
        }
        return false
    }

    private fun handleTouchEvent(x: Float, y: Float) {
        val xIndex = findIndex(x)
        val yIndex = findIndex(y)

        pixelList.find { it.x == xIndex && it.y == yIndex }.apply {
            this?.color = if (this?.color == PixelColor.BLACK) {
                PixelColor.WHITE
            } else {
                PixelColor.BLACK
            }
        }

        callback.onUpdate(pixelList)
    }

    private fun findIndex(x: Float): Int {
        return (x / pixelSize).toInt()
    }


}
