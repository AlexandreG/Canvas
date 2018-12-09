package fr.zzi.canvas.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

open class CachedDrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var bitmapCache: Bitmap? = null
    protected var canvasCache: Canvas? = null

    protected var viewWidth: Int = -1
    protected val paint: Paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            bitmapCache?.let {
                canvas.drawBitmap(bitmapCache, 0F, 0F, paint)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)

        initBitmapCache()
    }

    private fun initBitmapCache() {
        if (bitmapCache == null) {
            bitmapCache = Bitmap.createBitmap(viewWidth, viewWidth, Bitmap.Config.ARGB_8888)
            canvasCache = Canvas(bitmapCache)
        }
    }

}
