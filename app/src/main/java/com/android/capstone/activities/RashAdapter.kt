package com.android.capstone.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.capstone.R
import de.hdodenhof.circleimageview.CircleImageView

class RashAdapter(
    var rashNameList: ArrayList<String>,
    var detailsList: ArrayList<String>,
    var imageList: ArrayList<Int>,
    var context: Context) : RecyclerView.Adapter<RashAdapter.RashViewHolder>() {

    class RashViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var textViewRashName : TextView = itemView.findViewById(R.id.rashName)
        var textViewDetails : TextView = itemView.findViewById(R.id.rashInfo)
        var imageView : CircleImageView = itemView.findViewById(R.id.profile_image)
        var cardView : CardView = itemView.findViewById(R.id.cardView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RashViewHolder {

        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_design,parent,false)
        return RashViewHolder(view)
    }

    override fun onBindViewHolder(holder: RashViewHolder, position: Int) {

        holder.textViewRashName.text = rashNameList[position]
        holder.textViewDetails.text = detailsList[position]
        holder.imageView.setImageResource(imageList[position])
        val openURL = Intent(Intent.ACTION_VIEW)

        holder.cardView.setOnClickListener {
            if (rashNameList[position] == "Psoriasis") {
                openURL.data =
                    Uri.parse("https://www.webmd.com/skin-problems-and-treatments/psoriasis/default.htm")
                context.startActivity(openURL)
            } else if (rashNameList[position] == "Contact Dermatitis"){
                openURL.data =
                    Uri.parse("https://www.webmd.com/allergies/skin-allergies")
                context.startActivity(openURL)
            } else if (rashNameList[position] == "Acne or Rosacea"){
                openURL.data =
                    Uri.parse("https://www.webmd.com/skin-problems-and-treatments/acne/acne-treatments-that-work")
                context.startActivity(openURL)
            } else if (rashNameList[position] == "Hives"){
                openURL.data =
                    Uri.parse("https://www.webmd.com/skin-problems-and-treatments/guide/hives-urticaria-angioedema")
                context.startActivity(openURL)
            } else if (rashNameList[position] == "Fungal Infections"){
                openURL.data =
                    Uri.parse("https://www.webmd.com/skin-problems-and-treatments/guide/fungal-infections-skin")
                context.startActivity(openURL)
            } else if (rashNameList[position] == "Eczema"){
                openURL.data =
                    Uri.parse("https://www.webmd.com/skin-problems-and-treatments/eczema/treatments-for-you")
                context.startActivity(openURL)
            }
        }
    }

    override fun getItemCount(): Int {

        return  rashNameList.size

    }
}