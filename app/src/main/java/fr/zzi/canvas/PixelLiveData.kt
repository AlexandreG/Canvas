package fr.zzi.canvas

import android.arch.lifecycle.LiveData
import android.util.Log
import com.google.firebase.database.*
import fr.zzi.canvas.model.Pixel


class PixelLiveData : LiveData<MutableList<Pixel>>() {

    private val dbReference: DatabaseReference
    private val childListener: ChildEventListener
    private val valueListener: ValueEventListener

    init {
        dbReference = FirebaseDatabase.getInstance().getReference("pixelList")
        childListener = ListEventListener()
        valueListener = GlobalEventListener()
        value = mutableListOf()
    }

    override fun onActive() {
        super.onActive()
        dbReference.addChildEventListener(childListener)
//        dbReference.addValueEventListener(valueListener)
    }

    override fun onInactive() {
        super.onInactive()
        dbReference.removeEventListener(childListener)
//        dbReference.removeEventListener(valueListener)
    }

    fun updateData(data: Pixel) {
        dbReference.push().setValue(data)
    }

    private inner class ListEventListener : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {
            Log.e(PixelLiveData::class.toString(), "Can't listen to pixel query")
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            //TODO maj couleur
        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            //TODO dessiner nouveau
            val pixel = p0.getValue(Pixel::class.java)
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

    private inner class GlobalEventListener : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
//            value = dataSnapshot.getValue(MutableList<Pixel>::class.java)
//            value = hashmap?.values as MutableList<Pixel>?
        }


        override fun onCancelled(p0: DatabaseError) {
            Log.e(PixelLiveData::class.toString(), "Can't listen to pixel query")
        }
    }

}