package com.gavkariadmin.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.gavkariadmin.Model.ApiResponse
import com.gavkariadmin.Model.BuySale
import com.gavkariadmin.Model.SaleAd
import com.gavkariadmin.Model.SaleAdResponse
import com.gavkariadmin.R
import com.gavkariadmin.adapter.BuySaleAdAdapter
import com.gavkariadmin.constant.HttpConstant.SUCCESS
import com.gavkariadmin.Model.CommonResponse
import com.gavkariadmin.constant.AppConstant
import com.gavkariadmin.network.ApiClient
import com.gavkariadmin.network.ApiInterface
import com.gavkariadmin.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariadmin.Model.AcceptAdBody
import com.gavkariadmin.utility.InternetUtil
import com.gavkariadmin.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_sale.*
import kotlinx.android.synthetic.main.layout_no_data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BuySaleActivity : BaseActivity(), BuySaleAdAdapter.OnItemClickListener {


    private lateinit var mySaleAdResponse: SaleAdResponse

    lateinit var mySaleAdList: ArrayList<SaleAd>

    lateinit var mySaleAdAdapter: BuySaleAdAdapter

    lateinit var homeViewModel: HomeViewModel

    lateinit var observer: Observer<ApiResponse<SaleAdResponse, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale)
        setupToolbar(R.id.toolbar, getString(R.string.lbl_sale_ads))
        setupView()
    }

    override fun onResume() {
        super.onResume()
        accessData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onItemClick(saleAd: SaleAd) {

        var buySale = BuySale(saleAd.id,saleAd.user_id,saleAd.village_id,saleAd.status,saleAd.tab_type,
            saleAd.type,saleAd.name,saleAd.price,saleAd.breed,saleAd.pregnancies_count,saleAd.pregnancy_status,
            saleAd.milk,saleAd.weight,saleAd.company,saleAd.model,saleAd.year,saleAd.km_driven,saleAd.power,
            saleAd.capacity,saleAd.material,saleAd.tynes_count,saleAd.size,saleAd.phase,saleAd.latitude,
            saleAd.longitude,saleAd.village_en,saleAd.village_mr,saleAd.created_at,saleAd.photo,saleAd.title,
            saleAd.description,saleAd.fav_user_id,0.0,"MySaleAdActivity")

        startActivity(Intent(applicationContext,BuySaleDetailsActivity::class.java).putExtra("buySaleData",buySale))
    }

    override fun onClickDelete(saleAd: SaleAd) {
        showDeleteAlert(saleAd)
    }

    override fun onClickAccept(saleAd: SaleAd) {
        var acceptAdBody = AcceptAdBody(saleAd.id,saleAd.village_id,AppConstant.PUBLISHED)
        acceptAd(acceptAdBody)
    }

    override fun onClickReject(saleAd: SaleAd) {
        var acceptAdBody = AcceptAdBody(saleAd.id,saleAd.village_id,AppConstant.REJECTED)
        acceptAd(acceptAdBody)
    }

    private fun setupView() {
        homeViewModel = HomeViewModel()
        mySaleAdList = ArrayList<SaleAd>()
        initObserver()
        rvMySaleAd.applyVerticalWithDividerLinearLayoutManager()
    }

    private fun initObserver() {
        observer = Observer { t ->
            if (t?.response != null) {
                mySaleAdResponse = t.response!!
                if (mySaleAdResponse?.status == SUCCESS) {
                    displayData(mySaleAdResponse)
                } else {
                    nvMySaleAd.visibility = View.GONE
                    layNoDataMySaleAd.visibility = View.VISIBLE
                    layNoInternetMySaleAd.visibility = View.GONE
                    layNoDataText.text = getString(R.string.msg_create_ad)
                    dismissProgress()
                }

            } else {
                showError(t?.error!!)
                dismissProgress()
            }
        }
    }

    private fun accessData(){
        showProgress()
        if (InternetUtil.isInternetOn()) {
            homeViewModel.getAllVillageAds().observe(this, observer)
        } else {
            dismissProgress()
            waitForInternetConnection()
        }
    }

    private fun displayData(response: SaleAdResponse?) {
        mySaleAdList = response!!.SaleAds
        mySaleAdAdapter = BuySaleAdAdapter(mySaleAdList, this)
        nvMySaleAd.visibility = View.VISIBLE
        layNoDataMySaleAd.visibility = View.GONE
        rvMySaleAd.adapter = mySaleAdAdapter
        dismissProgress()
    }

    private fun showDeleteAlert(item: SaleAd) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.msg_alert))
        builder.setMessage(getString(R.string.qes_delete_ad))
        builder.setPositiveButton(getString(R.string.msg_yes)) { dialog, which ->
            dialog.cancel()
            deleteAd(item)
        }
        builder.setNegativeButton(getString(R.string.msg_no)) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun deleteAd(item: SaleAd) {
        showProgress()
        ApiClient.get().create(ApiInterface::class.java)
            .deleteAd(item.id)
            .enqueue(object : Callback<CommonResponse> {
                override fun onResponse(call: Call<CommonResponse>?, response: Response<CommonResponse>?) {
                    if (response!!.code() == 200) {
                        runOnUiThread {
                            dismissProgress()
                            mySaleAdList.remove(item)
                            rvMySaleAd.adapter?.notifyDataSetChanged()
                            if (response.body()!!.message == "deleted") {
                                showSuccess(getString(R.string.msg_delete_sale))
                            }
                        }
                    } else {
                        if (response.errorBody() != null) {
                            runOnUiThread {
                                dismissProgress()
                                showError(response.errorBody().toString())
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<CommonResponse>?, t: Throwable?) {
                    runOnUiThread {
                        dismissProgress()
                        showError(t.toString())
                    }
                }
            })
    }

    private fun acceptAd(acceptAdBody: AcceptAdBody) {
        showProgress()
        ApiClient.get().create(ApiInterface::class.java)
            .acceptAds(acceptAdBody)
            .enqueue(object : Callback<CommonResponse> {
                override fun onResponse(call: Call<CommonResponse>?, response: Response<CommonResponse>?) {
                    if (response!!.code() == 200) {
                        runOnUiThread {
                            dismissProgress()
                            if (response.body()!!.message == "Success") {
                                showSuccess(getString(R.string.lbl_status_changed))
                                onResume()
                            }
                        }
                    } else {
                        if (response.errorBody() != null) {
                            runOnUiThread {
                                dismissProgress()
                                showError(response.errorBody().toString())
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<CommonResponse>?, t: Throwable?) {
                    runOnUiThread {
                        dismissProgress()
                        showError(t.toString())
                    }
                }
            })
    }

    private fun waitForInternetConnection() {

        InternetUtil.observe(this, Observer { status ->
            if (status!!) {
                nvMySaleAd.visibility = View.VISIBLE
                layNoInternetMySaleAd.visibility = View.GONE
                layNoDataMySaleAd.visibility = View.GONE

            } else {
                layNoInternetMySaleAd.visibility = View.VISIBLE
                nvMySaleAd.visibility = View.GONE
                layNoDataMySaleAd.visibility = View.GONE

            }
        })
    }

}



