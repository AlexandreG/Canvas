package fr.zzi.canvas

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import fr.zzi.canvas.model.Pixel
import fr.zzi.canvas.model.PixelColor
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CanvasView.Callback {

    private lateinit var canvasView: CanvasView
    private lateinit var progressBar: View

    private lateinit var pixelLiveData: PixelLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initCanvasView()
        initLiveData()
        displayLoader()
    }

    private fun displayLoader() {
        progressBar = findViewById(R.id.progress_bar)
    }

    private fun initCanvasView() {
        canvasView = findViewById(R.id.canvas_view)
        canvasView.callback = this
    }

    private fun initLiveData() {
        pixelLiveData = PixelLiveData()
        pixelLiveData.observe(this, Observer { it ->
            if (it?.isNotEmpty() == true) {
                if (progressBar.visibility == View.VISIBLE) {
                    progressBar.visibility = View.GONE
                }

                canvasView.refresh(it)
            }
        })
    }

    fun updateColor(view: View?) {
        canvasView.currentColor = when (view?.id) {
            R.id.color_1 -> PixelColor.WHITE
            R.id.color_2 -> PixelColor.GREEN
            R.id.color_3 -> PixelColor.YELLOW
            R.id.color_4 -> PixelColor.ORANGE
            R.id.color_5 -> PixelColor.RED
            R.id.color_6 -> PixelColor.PURPLE
            R.id.color_7 -> PixelColor.BLUE
            R.id.color_8 -> PixelColor.BLUE_LIGHT
            R.id.color_9 -> PixelColor.GREY
            else -> PixelColor.BLACK
        }
        current_color.background = view?.background

    }

    override fun onUpdate(data: Pixel, previousPixelId: String?) {
        pixelLiveData.addPixel(data, previousPixelId)
    }
}
