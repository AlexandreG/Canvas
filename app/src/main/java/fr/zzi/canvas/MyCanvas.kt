package fr.zzi.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import fr.zzi.canvas.model.Pixel
import fr.zzi.canvas.model.PixelColor

class MyCanvas @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), View.OnTouchListener {

    interface Callback {
        fun onUpdate(data: Pixel, id: String?)
    }

    val NB_PIXEL_WIDTH = 20


    lateinit var callback: Callback

    private var pixelList: MutableList<Pixel> = mutableListOf()
    private val paint: Paint = Paint().apply { color = Color.BLACK }
    private var pixelSize: Int = -1

    init {
        setOnTouchListener(this)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            drawBackground(canvas)
            drawData(canvas)
        }
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
    }

    private fun drawData(canvas: Canvas) {
        pixelList
            .filter { it.color == PixelColor.BLACK }
            .forEach { drawPixel(canvas, it.x.toFloat(), it.y.toFloat()) }
    }

    private fun drawPixel(canvas: Canvas?, i: Float, j: Float) {
        canvas?.drawRect(i * pixelSize, j * pixelSize, (i + 1) * pixelSize, (j + 1) * pixelSize, paint)
    }

    fun refresh(newData: MutableList<Pixel>) {
        pixelList = newData
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        pixelSize = MeasureSpec.getSize(widthMeasureSpec) / NB_PIXEL_WIDTH
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN
            || event?.action == MotionEvent.ACTION_MOVE
        ) {
            handleTouchEvent(event.x, event.y)
        }
        return true
    }

    private fun handleTouchEvent(x: Float, y: Float) {
        val xIndex = findIndex(x)
        val yIndex = findIndex(y)

        val touchedPixel = pixelList.findLast { it.x == xIndex && it.y == yIndex }

        if (touchedPixel?.color == PixelColor.BLACK) {
            return
        }

        val pixelColor =
            if (touchedPixel?.color == PixelColor.BLACK) {
                PixelColor.WHITE
            } else {
                PixelColor.BLACK
            }
        val newPixel = Pixel(xIndex, yIndex, pixelColor)

        callback.onUpdate(newPixel, touchedPixel?.id)
    }

    private fun findIndex(x: Float): Int {
        return (x / pixelSize).toInt()
    }


}
