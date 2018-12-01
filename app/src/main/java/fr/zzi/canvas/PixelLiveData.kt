package fr.zzi.canvas

import android.arch.lifecycle.LiveData
import android.util.Log
import com.google.firebase.database.*
import fr.zzi.canvas.model.Pixel


class PixelLiveData : LiveData<MutableList<Pixel>>() {

    private val dbReference: DatabaseReference
    private val childListener: ChildEventListener

    init {
        dbReference = FirebaseDatabase.getInstance().getReference("pixelList")
        childListener = ListEventListener()
        value = mutableListOf()
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
            //TODO dessiner nouveau
            val pixel = p0.getValue(Pixel::class.java)
            pixel?.id = p0.key

            pixel?.let {
                value?.add(it)
            }

            value = value
        }

        override fun onChildRemoved(p0: DataSnapshot) {
            //TODO effacer ancien
//            value = p0.value as MutableList<Pixel>?
        }
    }
}