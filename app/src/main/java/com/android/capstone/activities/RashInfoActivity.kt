package com.android.capstone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.capstone.R
import com.android.capstone.databinding.ActivityRashInfoBinding

private lateinit var binding: ActivityRashInfoBinding

class RashInfoActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    var rashNameList = ArrayList<String>()
    var detailsList = ArrayList<String>()
    var imageList = ArrayList<Int>()

    lateinit var adapter: RashAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRashInfoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recyclerView = binding.recyclerView

        recyclerView.layoutManager = LinearLayoutManager(this@RashInfoActivity)

        rashNameList.add("Psoriasis")
        rashNameList.add("Eczema")
        rashNameList.add("Contact Dermatitis")
        rashNameList.add("Hives")
        rashNameList.add("Fungal Infections")
        rashNameList.add("Acne or Rosacea")

        detailsList.add("A condition in which skin cells build up and form scales and itchy, dry patches.")
        detailsList.add("An itchy inflammation of the skin.")
        detailsList.add("A skin rash caused by contact with a certain substance.")
        detailsList.add("A skin rash triggered by a reaction to food, medicine, or other irritants.")
        detailsList.add("Any disease caused by a fungus.")
        detailsList.add("A condition that causes small red bumps filled with pus.")

        imageList.add(R.drawable.psoriasis)
        imageList.add(R.drawable.eczema)
        imageList.add(R.drawable.contact)
        imageList.add(R.drawable.hives)
        imageList.add(R.drawable.fungal)
        imageList.add(R.drawable.acne)

        adapter = RashAdapter(rashNameList,detailsList,imageList,this)

        recyclerView.adapter = adapter

    }
}