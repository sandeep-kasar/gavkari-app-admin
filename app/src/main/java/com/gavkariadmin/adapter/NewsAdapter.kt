package com.gavkariadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gavkariadmin.R
import com.gavkariadmin.constant.HttpConstant
import com.gavkariadmin.viewHolder.NewsViewHolder
import com.gavkariadmin.Model.News
import com.gavkariadmin.constant.AppConstant.PUBLISHED
import com.gavkariadmin.constant.AppConstant.REJECTED
import com.gavkariadmin.constant.AppConstant.UNDER_REVIEW
import com.gavkariadmin.utility.Util
import java.util.*


class NewsAdapter(var myNewsResponse: ArrayList<News>,
                    var onItemClickListener: OnItemClickListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<NewsViewHolder>() {


    private lateinit var parentView : Context

    companion object {
        private const val NEWS_TYPE = 0
    }

    override fun onBindViewHolder(viewHolder: NewsViewHolder, position: Int) {

        if (getItemViewType(position) == NEWS_TYPE) {
            showNews(viewHolder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {

        var viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        viewHolder = when (viewType) {

            NEWS_TYPE -> {
                val viewEvent = inflater.inflate(R.layout.layout_news_row, parent, false)
                NewsViewHolder(viewEvent)
            }

            else -> {
                val view = inflater.inflate(R.layout.layout_news_row, parent, false)
                NewsViewHolder(view)
            }
        }

        parentView = viewHolder.itemView.context.applicationContext

        return viewHolder
    }

    override fun getItemCount(): Int {
        return myNewsResponse.size
    }

    override fun getItemViewType(position: Int): Int {
        return NEWS_TYPE
    }

    private fun showNews(viewHolder: NewsViewHolder, position: Int) {

        val data = myNewsResponse[position]
        viewHolder.tvTitle.text = data.title
        viewHolder.tvSubtitle.text = data.description

        var inputDate = Util.getFormatedDate(data.news_date,
            "yyyy-MM-dd", "EEE, d MMM, yyyy",viewHolder.itemView.resources)

        viewHolder.tvDate.text = inputDate

        when {
            data.status == UNDER_REVIEW -> {
                viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_in_review)
                viewHolder.tvStatus.setTextColor(viewHolder.itemView.resources.getColor(R.color.sinopia))
            }
            data.status == REJECTED -> {
                viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_rejected)
                viewHolder.tvStatus.setTextColor(viewHolder.itemView.resources.getColor(R.color.sinopia))
                viewHolder.btnReject.visibility = View.GONE
                viewHolder.btnAccept.visibility = View.VISIBLE
            }
            data.status == PUBLISHED -> {
                viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_published)
                viewHolder.btnReject.visibility = View.VISIBLE
                viewHolder.btnAccept.visibility = View.GONE
            }
        }

        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.ic_gaav_logo_final_circle_pl_square)

        Glide.with(viewHolder.itemView.context)
            .setDefaultRequestOptions(requestOptions)
            .load(HttpConstant.BASE_NEWS_DOWNLOAD_URL +data.photo)
            .thumbnail(0.5f)
            .into(viewHolder.imgCard)

        viewHolder.itemView.setOnClickListener { onItemClickListener.onItemClick(data) }
        viewHolder.btnAccept.setOnClickListener { onItemClickListener.onClickAccept(data) }
        viewHolder.btnReject.setOnClickListener { onItemClickListener.onClickReject(data) }
        viewHolder.btnDelete.setOnClickListener { onItemClickListener.onClickDelete(data) }

    }

    interface OnItemClickListener {
        fun onItemClick(item: News)
        fun onClickDelete(item: News)
        fun onClickAccept(item: News)
        fun onClickReject(item: News)
    }

}