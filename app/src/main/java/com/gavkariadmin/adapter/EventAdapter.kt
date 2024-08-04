package com.gavkariadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gavkariadmin.R
import com.gavkariadmin.constant.HttpConstant
import com.gavkariadmin.Model.AllEvent
import com.gavkariadmin.EventViewHolder
import com.gavkariadmin.constant.AppConstant.CREATED
import com.gavkariadmin.constant.AppConstant.ENGAGEMENT
import com.gavkariadmin.constant.AppConstant.PUBLISHED
import com.gavkariadmin.constant.AppConstant.REJECTED
import com.gavkariadmin.constant.AppConstant.UNDER_REVIEW
import com.gavkariadmin.constant.AppConstant.WEDDING
import com.gavkariadmin.utility.Util


class EventAdapter(var myAdResponse: ArrayList<AllEvent>,
                   var onItemClickListener: OnItemClickListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<EventViewHolder>() {


    private lateinit var parentView : Context

    companion object {
        private const val TYPE_EVENT = 0
        private var tsLong: Long = System.currentTimeMillis()

    }

    override fun onBindViewHolder(viewHolder: EventViewHolder, position: Int) {

        if (getItemViewType(position) == TYPE_EVENT) {
            showEvent(viewHolder, position)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {

        var viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        viewHolder = when (viewType) {

            TYPE_EVENT -> {
                val viewEvent = inflater.inflate(R.layout.layout_event_row, parent, false)
                EventViewHolder(viewEvent)
            }

            else -> {
                val view = inflater.inflate(R.layout.layout_event_row, parent, false)
                EventViewHolder(view)
            }
        }

        parentView = viewHolder.itemView.context.applicationContext

        return viewHolder
    }

    override fun getItemCount(): Int {
        return myAdResponse.size
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_EVENT
    }

    private fun showEvent(viewHolder: EventViewHolder, position: Int) {

        val data = myAdResponse[position]
        viewHolder.tvTitle.text = data.title
        viewHolder.tvSubtitle.text = data.subtitle

        if(data.type== WEDDING || data.type== ENGAGEMENT){
            var subtitle = data.subtitle +" "+ data.subtitle_one +" "+parentView.getString(R.string.lbl_and)+" "+
                    data.subtitle_three +" "+data.subtitle_four +" "+parentView.getString(R.string.lbl_there_wedding)
            viewHolder.tvSubtitle.text = subtitle
        }else{
            viewHolder.tvSubtitle.text = data.subtitle
        }

        var inputDate = Util.getFormatedDate(data.event_date,
                "yyyy-MM-dd", "EEE, MMM d, yyyy",viewHolder.itemView.resources)

        viewHolder.tvDate.text = inputDate
        
        var millisecond = data.event_date_ms.toLong()

        if (data.status == UNDER_REVIEW){
            viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_in_review)
            viewHolder.tvStatus.setTextColor(viewHolder.itemView.resources.getColor(R.color.sinopia))
        }

        if (data.status == REJECTED){
            viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_rejected)
            viewHolder.tvStatus.setTextColor(viewHolder.itemView.resources.getColor(R.color.sinopia))
        }

        if (data.status == PUBLISHED){
            viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_published)
            viewHolder.btnAccept.visibility = View.GONE
        }

        if (data.status == CREATED){
            viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_not_published)
            viewHolder.tvStatus.setTextColor(viewHolder.itemView.resources.getColor(R.color.sinopia))
            viewHolder.btnAccept.visibility = View.GONE
            viewHolder.btnReject.visibility = View.GONE
            viewHolder.btnDelete.visibility = View.GONE
        }

        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.ic_gaav_logo_final_circle_pl_square)
        Glide.with(viewHolder.itemView.context)
                .setDefaultRequestOptions(requestOptions)
                .load(HttpConstant.BASE_EVENT_DOWNLOAD_URL +data.photo)
                .thumbnail(0.5f)
                .into(viewHolder.imgCard)

        viewHolder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(data)
        }

        if (millisecond < tsLong){
            viewHolder.btnAccept.visibility = View.GONE
            viewHolder.btnReject.visibility = View.GONE
            viewHolder.btnDelete.visibility = View.VISIBLE
        }

        viewHolder.btnAccept.setOnClickListener { onItemClickListener.onClickAccept(data) }
        viewHolder.btnReject.setOnClickListener { onItemClickListener.onClickReject(data) }
        viewHolder.btnDelete.setOnClickListener { onItemClickListener.onClickDelete(data) }
    }

    interface OnItemClickListener {
        fun onItemClick(item: AllEvent)
        fun onClickDelete(item: AllEvent)
        fun onClickAccept(item: AllEvent)
        fun onClickReject(item: AllEvent)

    }

}