package com.gavkariadmin

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class EventViewHolder(itemview: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemview) {

    var imgCard: ImageView = itemview.findViewById(R.id.imgCard)
    var tvTitle: TextView = itemview.findViewById(R.id.tvTitle)
    var tvSubtitle: TextView = itemview.findViewById(R.id.tvSubtitle)
    var tvDate: TextView = itemview.findViewById(R.id.tvDate)
    var tvStatus: TextView = itemview.findViewById(R.id.tvStatus)
    var btnDelete:Button = itemView.findViewById(R.id.btnDelete)
    var btnAccept:Button = itemView.findViewById(R.id.btnAccept)
    var btnReject:Button = itemView.findViewById(R.id.btnReject)

}