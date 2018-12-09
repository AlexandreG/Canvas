package fr.zzi.canvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import fr.zzi.canvas.model.Pixel

class CanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), View.OnTouchListener {

    interface Callback {
        fun onTouch(xIndex: Int, yIndex: Int)
    }

    companion object {
        val NB_PIXEL_WIDTH = 50
        val FPS = 20
    }

    lateinit var callback: Callback

    private var bitmapCache: Bitmap? = null
    private var canvasCache: Canvas? = null

    private val newPixels: MutableList<Pixel>
    private val paint: Paint
    private var pixelSize: Int
    private var viewWidth: Int = -1

    init {
        newPixels = mutableListOf()
        paint = Paint()
        pixelSize = -1
        setOnTouchListener(this)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            bitmapCache?.let {
                canvas.drawBitmap(bitmapCache, 0F, 0F, paint)
            }

            drawAddedPixels(canvas)
            newPixels.clear()
        }
    }

    private fun drawAddedPixels(canvas: Canvas) {
        newPixels
            .forEach {
                drawPixel(canvas, it)
                drawPixel(canvasCache, it)
            }
    }

    private fun drawPixel(canvas: Canvas?, pixel: Pixel) {
        val i = pixel.x.toFloat()
        val j = pixel.y.toFloat()
        paint.color = ContextCompat.getColor(context, pixel.color.colorId)
        canvas?.drawRect(i * pixelSize, j * pixelSize, (i + 1) * pixelSize, (j + 1) * pixelSize, paint)
    }

    fun refresh(addedPixels: MutableList<Pixel>) {
        newPixels.addAll(addedPixels)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        pixelSize = viewWidth / NB_PIXEL_WIDTH

        initBitmapCache()
    }

    private fun initBitmapCache() {
        if (bitmapCache == null) {
            bitmapCache = Bitmap.createBitmap(viewWidth, viewWidth, Bitmap.Config.ARGB_8888)
            canvasCache = Canvas(bitmapCache)
        }
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
        callback.onTouch(xIndex, yIndex)
    }

    private fun findIndex(x: Float): Int {
        return (x / pixelSize).toInt()
    }


}
