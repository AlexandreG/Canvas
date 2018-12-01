package fr.zzi.canvas

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import fr.zzi.canvas.model.Pixel


class MainActivity : AppCompatActivity(), MyCanvas.Callback {

    lateinit var canvas: MyCanvas
    lateinit var pixelLiveData: PixelLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initCanvas()
        initLiveData()
    }

    private fun initCanvas() {
        canvas = findViewById(R.id.my_canvas)
        canvas.callback = this
    }

    private fun initLiveData() {
        pixelLiveData = PixelLiveData()
        pixelLiveData.observe(this, Observer { it ->
            it?.let {
                canvas.refresh(it)
            }
        })
    }

    override fun onUpdate(data: Pixel, previousPixelId: String?) {
        pixelLiveData.addPixel(data, previousPixelId)
    }
}
