package fr.zzi.canvas.network

import android.arch.lifecycle.LiveData
import android.util.Log
import com.google.firebase.database.*


class OnlineUserLiveData : LiveData<Int>() {

    private val dbReference: DatabaseReference
    private val firebaseListener: ValueEventListener

    var onlineUserNb: Int = 0
    var initialized = false

    init {
        dbReference = FirebaseDatabase.getInstance().getReference("onlineUserNb")

        firebaseListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e(OnlineUserLiveData::class.toString(), "Can't listen to online user query")
            }

            override fun onDataChange(p0: DataSnapshot) {
                onlineUserNb = p0.getValue(Int::class.java) ?: 0

                if (!initialized) {
                    initialized = true
                    onConnected()
                } else {
                    value = onlineUserNb
                }
            }
        }
    }

    fun onConnected() {
        if (initialized) {
            ++onlineUserNb
            dbReference.setValue(onlineUserNb)
        }
    }

    fun onDisconnected() {
        --onlineUserNb
        if (onlineUserNb < 0) {
            onlineUserNb = 0
        }
        dbReference.setValue(onlineUserNb)
    }

    override fun onActive() {
        super.onActive()
        dbReference.addValueEventListener(firebaseListener)
    }

    override fun onInactive() {
        super.onInactive()
        dbReference.removeEventListener(firebaseListener)
    }
}