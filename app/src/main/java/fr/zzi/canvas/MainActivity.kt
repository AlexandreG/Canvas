package fr.zzi.canvas

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import fr.zzi.canvas.model.Pixel
import fr.zzi.canvas.model.PixelLiveData


class MainActivity : AppCompatActivity(), MyCanvas.Callback {
    lateinit var canvas: MyCanvas

    lateinit var pixelLiveData: PixelLiveData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        canvas = findViewById(R.id.my_canvas)
        canvas.callback = this

        pixelLiveData = PixelLiveData()
        pixelLiveData.observe(this, Observer { it ->
            it?.let {
                canvas.refresh(it)
            }
        })
    }

    override fun onUpdate(data: List<Pixel>) {
        pixelLiveData.updateData(data)
    }
}
