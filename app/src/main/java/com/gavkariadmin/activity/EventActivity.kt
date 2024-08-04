package com.gavkariadmin.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.gavkariadmin.Model.ApiResponse
import com.gavkariadmin.R
import com.gavkariadmin.constant.HttpConstant.SUCCESS
import com.gavkariadmin.Model.*
import com.gavkariadmin.adapter.EventAdapter
import com.gavkariadmin.constant.AppConstant
import com.gavkariadmin.network.ApiClient
import com.gavkariadmin.network.ApiInterface
import com.gavkariadmin.utility.InternetUtil
import com.gavkariadmin.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariadmin.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.layout_no_data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventActivity : BaseActivity(), EventAdapter.OnItemClickListener {

    private lateinit var myAdResponse: EventResponse

    lateinit var myEvent: ArrayList<AllEvent>

    lateinit var myAdAdapter: EventAdapter

    lateinit var homeViewModel: HomeViewModel

    lateinit var observer: Observer<ApiResponse<EventResponse, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        setupToolbar(R.id.toolbar, getString(R.string.title_my_ad))
        setupView()

    }

    override fun onResume() {
        super.onResume()
        accessData()
    }

    override fun onItemClick(item: AllEvent) {
        startActivityForResult(
            Intent(applicationContext, EventDetailActivity::class.java)
                .putExtra("VillageResponse", item), AppConstant.OPEN_MY_AD_DETAIL_ACTIVITY
        )
    }

    override fun onClickDelete(item: AllEvent) {
        showDeleteAlert(item)
    }

    override fun onClickAccept(event_obj: AllEvent) {
        var acceptAdBody = AcceptEventBody(event_obj.id,event_obj.village_id,AppConstant.PUBLISHED)
        acceptAd(acceptAdBody)
    }

    override fun onClickReject(evnet_obj: AllEvent) {
        var acceptAdBody = AcceptEventBody(evnet_obj.id,evnet_obj.village_id,AppConstant.REJECTED)
        acceptAd(acceptAdBody)
    }

    private fun acceptAd(acceptEventBody: AcceptEventBody) {
        showProgress()
        ApiClient.get().create(ApiInterface::class.java)
            .acceptEvent(acceptEventBody)
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(applicationContext, HomeActivity::class.java))
        overridePendingTransition(0, 0)
        ActivityCompat.finishAffinity(this@EventActivity)

    }

    private fun setupView() {
        homeViewModel = HomeViewModel()
        initObserver()
        rvMyAd.applyVerticalWithDividerLinearLayoutManager()
    }

    private fun initObserver() {
        observer = Observer { t ->

            if (t?.response != null) {

                myAdResponse = t.response!!

                if (myAdResponse?.status == SUCCESS) {
                    displayData(myAdResponse)
                } else {
                    nvMyAd.visibility = View.GONE
                    layNoDataMyad.visibility = View.VISIBLE
                    layNoInternetMyAd.visibility = View.GONE
                    layNoDataText.text=getString(R.string.msg_create_evnt_first)
                    dismissProgress()
                }

            } else {
                showError(t?.error!!)
                dismissProgress()
            }
        }
    }

    private fun accessData() {

        showProgress()

        if (InternetUtil.isInternetOn()) {
            homeViewModel.getAllVillageEvents().observe(this, observer)
        } else {
            dismissProgress()
            waitForInternetConnection()
        }
    }

    private fun displayData(response: EventResponse?) {
        myEvent = response!!.AllEvent
        myAdAdapter = EventAdapter(myEvent, this)
        nvMyAd.visibility = View.VISIBLE
        layNoDataMyad.visibility = View.GONE
        rvMyAd.adapter = myAdAdapter
        dismissProgress()

    }

    private fun deleteEvent(item: AllEvent) {
        showProgress()
        ApiClient.get().create(ApiInterface::class.java)
            .deleteEvent(item.id)
            .enqueue(object : Callback<CommonResponse> {
                override fun onResponse(call: Call<CommonResponse>?, response: Response<CommonResponse>?) {
                    if (response!!.code() == 200) {
                        runOnUiThread {
                            dismissProgress()
                            myEvent.remove(item)
                            rvMyAd.adapter?.notifyDataSetChanged()
                            if (response.body()!!.message == "deleted") {
                                showSuccess(getString(R.string.msg_delete_ad))
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

    private fun showDeleteAlert(item: AllEvent) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.msg_alert))
        builder.setMessage(getString(R.string.msg_delete_alert))
        builder.setPositiveButton(getString(R.string.msg_yes)) { dialog, which ->
            dialog.cancel()
            deleteEvent(item)
        }
        builder.setNegativeButton(getString(R.string.msg_no)) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun waitForInternetConnection() {

        InternetUtil.observe(this, Observer { status ->
            if (status!!) {
                nvMyAd.visibility = View.VISIBLE
                layNoInternetMyAd.visibility = View.GONE
                layNoDataMyad.visibility = View.GONE

            } else {
                layNoInternetMyAd.visibility = View.VISIBLE
                nvMyAd.visibility = View.GONE
                layNoDataMyad.visibility = View.GONE

            }
        })
    }
}
