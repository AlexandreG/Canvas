package fr.zzi.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class MyCanvas @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), View.OnTouchListener {

    interface Callback {
        fun onUpdate(data: MutableList<MutableList<Int>>)
    }

    companion object {
        const val PIXEL_NUMBER = 100
    }

    private var data: MutableList<MutableList<Int>> =
        mutableListOf(
            mutableListOf(1, 1, 1, 0),
            mutableListOf(0, 0, 0, 1),
            mutableListOf(0, 1, 0, 1),
            mutableListOf(1, 0, 0, 1)
        )

    lateinit var callback: Callback
    private val paint = Paint()
    private var pixelSize = 0

    init {
        paint.color = Color.BLACK
        setOnTouchListener(this)
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

    fun refresh(newData: MutableList<MutableList<Int>>) {
        data = newData
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        pixelSize = MeasureSpec.getSize(widthMeasureSpec) / PIXEL_NUMBER
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

        data[xIndex][yIndex] = when (data[xIndex][yIndex]) {
            1 -> 0
            else -> 1
        }
        callback.onUpdate(data)
    }

    private fun findIndex(x: Float): Int {
        return (x / pixelSize).toInt()
    }


}
