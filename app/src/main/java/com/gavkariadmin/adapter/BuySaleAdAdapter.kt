package com.gavkariadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gavkariadmin.Model.SaleAd
import com.gavkariadmin.R
import com.gavkariadmin.constant.HttpConstant
import com.gavkariadmin.viewHolder.BuySaleAdHolder
import com.gavkariadmin.constant.AppConstant.ANIMAL
import com.gavkariadmin.constant.AppConstant.BUFFALO
import com.gavkariadmin.constant.AppConstant.CAR
import com.gavkariadmin.constant.AppConstant.COW
import com.gavkariadmin.constant.AppConstant.CULTIVATOR
import com.gavkariadmin.constant.AppConstant.EQUIPMENTS
import com.gavkariadmin.constant.AppConstant.GOAT
import com.gavkariadmin.constant.AppConstant.HEIFER_BUFFALO
import com.gavkariadmin.constant.AppConstant.HEIFER_COW
import com.gavkariadmin.constant.AppConstant.IRRIGATION_MATERIAL
import com.gavkariadmin.constant.AppConstant.KUTTI_MACHINE
import com.gavkariadmin.constant.AppConstant.MACHINERY
import com.gavkariadmin.constant.AppConstant.MALE_BUFFALO
import com.gavkariadmin.constant.AppConstant.MALE_GOAT
import com.gavkariadmin.constant.AppConstant.OTHER_DOMESTIC_ANIMALS
import com.gavkariadmin.constant.AppConstant.OX
import com.gavkariadmin.constant.AppConstant.PICKUP
import com.gavkariadmin.constant.AppConstant.PLOUGH
import com.gavkariadmin.constant.AppConstant.PUBLISHED
import com.gavkariadmin.constant.AppConstant.REJECTED
import com.gavkariadmin.constant.AppConstant.ROTOVATOR
import com.gavkariadmin.constant.AppConstant.SEED_DRILL
import com.gavkariadmin.constant.AppConstant.SPRAY_BLOWER
import com.gavkariadmin.constant.AppConstant.STEEL
import com.gavkariadmin.constant.AppConstant.TEMPO_TRUCK
import com.gavkariadmin.constant.AppConstant.THRESHER
import com.gavkariadmin.constant.AppConstant.TRACTOR
import com.gavkariadmin.constant.AppConstant.TROLLEY
import com.gavkariadmin.constant.AppConstant.TWO_WHEELER
import com.gavkariadmin.constant.AppConstant.UNDER_REVIEW
import com.gavkariadmin.constant.AppConstant.WATER_MOTOR_PUMP
import com.gavkariadmin.utility.Util


class BuySaleAdAdapter(var myVillageResponse: ArrayList<SaleAd>,
                       var onItemClickListener: OnItemClickListener) : androidx.recyclerview.widget.RecyclerView.Adapter<BuySaleAdHolder>() {

    companion object {
        private const val TYPE_BUY_SALE = 0
    }

    private lateinit var parentView : Context

    override fun onBindViewHolder(viewHolder: BuySaleAdHolder, position: Int) {
        if (getItemViewType(position) == TYPE_BUY_SALE) {
            showBuySale(viewHolder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuySaleAdHolder {

        var viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        viewHolder = when (viewType) {

            TYPE_BUY_SALE -> {
                val viewEvent = inflater.inflate(R.layout.layout_sale_ad_row, parent, false)
                BuySaleAdHolder(viewEvent)
            }

            else -> {
                val view = inflater.inflate(R.layout.layout_sale_ad_row, parent, false)
                BuySaleAdHolder(view)
            }
        }

        parentView = parent.context

        return viewHolder
    }

    override fun getItemCount(): Int {
        return myVillageResponse.size
    }

    override fun getItemViewType(position: Int): Int {
        val obj = myVillageResponse[position]
        return TYPE_BUY_SALE
        return super.getItemViewType(position)
    }

    private fun showBuySale(viewHolder: BuySaleAdHolder, position: Int) {

        val data = myVillageResponse[position] as SaleAd

        viewHolder.tvAddress.text = data.village_mr
        viewHolder.tvPrice.text = data.price

        Glide.with(viewHolder.itemView.context)
                .load(HttpConstant.BASE_BUYSALE_DOWNLOAD_URL +data.photo)
                .thumbnail(0.5f)
                .into(viewHolder.imgCard)

        var inputDate = Util.getFormatedDate(data.created_at,
                "yyyy-MM-dd HH:MM:SS", "d MMM, yyyy",viewHolder.itemView.resources)

        viewHolder.tvDate.text = inputDate

        viewHolder.itemView.setOnClickListener {onItemClickListener.onItemClick(data)}
        viewHolder.btnDelete.setOnClickListener {onItemClickListener.onClickDelete(data)}
        viewHolder.btnReject.setOnClickListener {onItemClickListener.onClickReject(data)}
        viewHolder.btnAccept.setOnClickListener {onItemClickListener.onClickAccept(data)}

        when {
            data.tab_type == ANIMAL -> {

                if (data.type == COW || data.type == BUFFALO || data.type == HEIFER_COW ||
                        data.type == HEIFER_BUFFALO || data.type == GOAT ){

                    viewHolder.tvType.text = data.name + ","

                    if (data.type == GOAT){
                        viewHolder.tvBrand.visibility = View.GONE
                    }else{
                        viewHolder.tvBrand.text = data.breed
                    }

                    var preg_status = parentView.getString(R.string.lbl_not_pregnant)
                    var milk = parentView.getString(R.string.lbl_no_milk)

                    if (data.pregnancy_status > 0){
                        preg_status = parentView.getString(R.string.lbl_pregnant)
                    }
                    if (data.milk >0){
                        milk = data.milk.toString() +" "+ parentView.getString(R.string.lbl_milk_liter)
                    }

                    viewHolder.tvDetails.text = preg_status +",  "+ milk

                }else if (data.type == OX || data.type == MALE_BUFFALO || data.type == OTHER_DOMESTIC_ANIMALS){
                    viewHolder.tvType.text = data.name
                    viewHolder.tvDetails.text = data.title
                    viewHolder.tvBrand.visibility = View.INVISIBLE

                }else if (data.type == MALE_GOAT){
                    viewHolder.tvType.text = data.name
                    viewHolder.tvDetails.text = data.weight + " " + parentView.getString(R.string.lbl_kg)
                    viewHolder.tvBrand.visibility = View.INVISIBLE
                }

            }
            data.tab_type == MACHINERY -> {

                viewHolder.tvType.text = data.name + ","
                viewHolder.tvBrand.text = data.company

                var year = data.year + ", "
                var extra_info = ""

                if (data.type == TRACTOR || data.type == PICKUP || data.type == TEMPO_TRUCK ||
                        data.type == CAR || data.type == TWO_WHEELER ){

                    extra_info = data.km_driven +" "+ parentView.getString(R.string.lbl_km)

                }else if (data.type == THRESHER || data.type == KUTTI_MACHINE){

                    extra_info = data.power

                }else if (data.type == SPRAY_BLOWER){

                    extra_info = data.capacity +" "+ parentView.getString(R.string.lbl_capacity)

                }else{
                    extra_info = data.title
                }

                viewHolder.tvDetails.text = year + extra_info
            }
            data.tab_type == EQUIPMENTS -> {

                viewHolder.tvType.text = data.name + ","
                viewHolder.tvBrand.text = data.company

                var year = data.year + ", "
                var extra_info = ""

                if (data.type == CULTIVATOR || data.type == SEED_DRILL){

                    extra_info = data.tynes_count +" "+ parentView.getString(R.string.lbl_tynes)

                }else if (data.type == ROTOVATOR){

                    extra_info = data.material

                }else if (data.type == PLOUGH){

                    extra_info = data.weight + " " + parentView.getString(R.string.lbl_kg)

                }else if (data.type == TROLLEY){

                    extra_info = data.capacity

                }else if (data.type == WATER_MOTOR_PUMP){

                    extra_info = data.phase + ", " + data.power

                }else if (data.type == IRRIGATION_MATERIAL || data.type == STEEL){

                    extra_info = data.title

                }else {

                    extra_info = data.title
                }

                viewHolder.tvDetails.text = year + extra_info

            }
            else -> {}
        }

        when {
            data.status == UNDER_REVIEW -> {
                viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_in_review)
                viewHolder.tvStatus.setTextColor(viewHolder.itemView.resources.getColor(R.color.sinopia))
            }
            data.status == REJECTED -> {
                viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_rejected)
                viewHolder.tvStatus.setTextColor(viewHolder.itemView.resources.getColor(R.color.sinopia))
            }
            data.status == PUBLISHED -> {
                viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_published)
                viewHolder.btnAccept.visibility = View.GONE
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(item: SaleAd)
        fun onClickDelete(item: SaleAd)
        fun onClickAccept(item: SaleAd)
        fun onClickReject(item: SaleAd)
    }
}

///home/sandeep/gavkariapp/Api/api/application/
