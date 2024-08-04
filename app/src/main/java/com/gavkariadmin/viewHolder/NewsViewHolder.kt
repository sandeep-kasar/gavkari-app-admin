package com.gavkariadmin.viewHolder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gavkariadmin.R

class NewsViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

    var imgCard: ImageView = itemview.findViewById(R.id.imgCard)
    var tvTitle: TextView = itemview.findViewById(R.id.tvTitle)
    var tvSubtitle: TextView = itemview.findViewById(R.id.tvSubtitle)
    var tvStatus: TextView = itemview.findViewById(R.id.tvStatus)
    var tvDate: TextView = itemview.findViewById(R.id.tvDate)
    var btnDelete: Button = itemview.findViewById(R.id.btnDelete)
    var btnAccept:Button = itemView.findViewById(R.id.btnAccept)
    var btnReject:Button = itemView.findViewById(R.id.btnReject)

}