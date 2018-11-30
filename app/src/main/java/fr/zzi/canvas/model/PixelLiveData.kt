package fr.zzi.canvas.model

import android.arch.lifecycle.LiveData
import android.util.Log
import com.google.firebase.database.*

class PixelLiveData : LiveData<List<Pixel>>() {

    private val database: DatabaseReference
    private val listener: EventListener

    init {
        database = FirebaseDatabase.getInstance().getReference("canvas")
        listener = EventListener()
    }

    override fun onActive() {
        super.onActive()
        database.addValueEventListener(listener)
    }

    override fun onInactive() {
        super.onInactive()
        database.removeEventListener(listener)
    }

    fun updateData(data: List<Pixel>) {
        value = data
    }

    private inner class EventListener : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            value = dataSnapshot.value as List<Pixel>?
        }

        override fun onCancelled(p0: DatabaseError) {
            Log.e(PixelLiveData::class.toString(), "Can't listen to pixel query")
        }

    }

}