package fr.zzi.canvas.network

import android.arch.lifecycle.LiveData
import android.util.Log
import com.google.firebase.database.*
import fr.zzi.canvas.model.Pixel


class PixelLiveData : LiveData<Pixel>() {

    private val dbReference: DatabaseReference
    private val childListener: ChildEventListener

    init {
        dbReference = FirebaseDatabase.getInstance().getReference("pixelList")
        childListener = ListEventListener()
    }

    override fun onActive() {
        super.onActive()
        dbReference.addChildEventListener(childListener)
    }

    override fun onInactive() {
        super.onInactive()
        dbReference.removeEventListener(childListener)
    }

    fun addPixel(pixel: Pixel, previousPixelId: String?) {
        previousPixelId?.let { dbReference.child(it).removeValue() }
        dbReference.push().setValue(pixel)
    }

    private inner class ListEventListener : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {
            Log.e(PixelLiveData::class.toString(), "Can't listen to pixel query")
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            //not used
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            //not used
        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            val pixel = p0.getValue(Pixel::class.java)
            pixel?.id = p0.key
            value = pixel
        }

        override fun onChildRemoved(p0: DataSnapshot) {
            //No need to erase an old pixel :
            //we receive the new pixel to draw with onChildAdded()
        }
    }
}