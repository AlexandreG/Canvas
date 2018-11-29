package fr.zzi.canvas

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.*


class MainActivity : AppCompatActivity(), MyCanvas.Callback {

    lateinit var canvas: MyCanvas
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        canvas = findViewById(R.id.my_canvas)
        canvas.callback = this

        database = FirebaseDatabase.getInstance().getReference("canvas")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.value?.let {
                    canvas.refresh(dataSnapshot.value as MutableList<MutableList<Int>>)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }

        })

    }

    override fun onUpdate(data: MutableList<MutableList<Int>>) {
        database.setValue(data)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        var data: MutableList<MutableList<Int>> = mutableListOf()
        for (i in 0..99) {
            data.add(mutableListOf())
            for (j in 0..99) {
                data[i].add(j, 0)
            }
        }

        database.setValue(data)
    }
}
