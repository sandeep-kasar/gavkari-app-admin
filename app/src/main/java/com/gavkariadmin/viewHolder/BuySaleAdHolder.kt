package com.gavkariadmin.viewHolder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gavkariadmin.R

class BuySaleAdHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var imgCard :ImageView = itemView.findViewById(R.id.imgCard)
    var tvType :TextView = itemView.findViewById(R.id.tvType)
    var tvBrand :TextView = itemView.findViewById(R.id.tvBrand)
    var tvDetails :TextView = itemView.findViewById(R.id.tvDetails)
    var tvAddress:TextView = itemView.findViewById(R.id.tvAddress)
    var tvPrice:TextView = itemView.findViewById(R.id.tvPrice)
    var tvDate:TextView = itemView.findViewById(R.id.tvDate)
    var tvStatus:TextView = itemView.findViewById(R.id.tvStatus)
    var btnDelete:Button = itemView.findViewById(R.id.btnDelete)
    var btnAccept:Button = itemView.findViewById(R.id.btnAccept)
    var btnReject:Button = itemView.findViewById(R.id.btnReject)
}