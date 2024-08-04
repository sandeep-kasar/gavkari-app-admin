package com.gavkariadmin.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.gavkariadmin.Model.ApiResponse
import com.gavkariadmin.R
import com.gavkariadmin.adapter.NewsAdapter
import com.gavkariadmin.constant.HttpConstant.SUCCESS
import com.gavkariadmin.Model.*
import com.gavkariadmin.constant.AppConstant
import com.gavkariadmin.network.ApiClient
import com.gavkariadmin.network.ApiInterface
import com.gavkariadmin.utility.InternetUtil
import com.gavkariadmin.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariadmin.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.layout_no_data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewsActivity : BaseActivity(), NewsAdapter.OnItemClickListener {

    private lateinit var newsResponse: NewsResponse

    lateinit var news: ArrayList<News>

    lateinit var newsAdapter: NewsAdapter

    lateinit var homeViewModel: HomeViewModel

    lateinit var observer: Observer<ApiResponse<NewsResponse, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        setupToolbar(R.id.toolbar, getString(R.string.lbl_my_news))
        setupView()

    }

    override fun onItemClick(myNews: News) {
        var news = News(myNews.id,myNews.user_id,myNews.village_id,myNews.assembly_const_id,myNews.parliament_const_id,myNews.news_type,
            myNews.status,myNews.news_date,myNews.news_date_ms,myNews.title,myNews.source,myNews.photo,myNews.short_description,myNews.description)
        startActivity(Intent(applicationContext, NewsDetailActivity::class.java).putExtra("news_data", news))

    }

    override fun onClickDelete(item: News) {
        showDeleteAlert(item)
    }

    override fun onClickAccept(news_obj: News) {
        var acceptNewsBody = AcceptNewsBody(news_obj.id,news_obj.village_id, AppConstant.PUBLISHED)
        acceptAd(acceptNewsBody )
    }

    override fun onClickReject(news_obj: News) {
        var acceptNewsBody = AcceptNewsBody(news_obj.id,news_obj.village_id, AppConstant.REJECTED)
        acceptAd(acceptNewsBody )
    }

    private fun acceptAd(acceptNewsBody: AcceptNewsBody) {
        showProgress()
        ApiClient.get().create(ApiInterface::class.java)
            .acceptNews(acceptNewsBody)
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

    override fun onResume() {
        super.onResume()
        accessData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(applicationContext, HomeActivity::class.java))
        overridePendingTransition(0, 0)
        ActivityCompat.finishAffinity(this@NewsActivity)

    }

    private fun setupView() {

        homeViewModel = HomeViewModel()
        initObserver()
        rvMyNews.applyVerticalWithDividerLinearLayoutManager()
    }

    private fun initObserver() {
        observer = Observer { t ->

            if (t?.response != null) {

                newsResponse = t.response!!

                if (newsResponse?.status == SUCCESS) {
                    displayData(newsResponse)
                } else {
                    nvMyNews.visibility = View.GONE
                    layNoDataMyNews.visibility = View.VISIBLE
                    layNoInternetMyNews.visibility = View.GONE
                    layNoDataText.text=getString(R.string.lml_no_news_yet)
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
            homeViewModel.getAllNews().observe(this, observer)
        } else {
            dismissProgress()
            waitForInternetConnection()
        }
    }

    private fun displayData(response: NewsResponse?) {
        news = response!!.AllNews
        newsAdapter = NewsAdapter(news, this)
        nvMyNews.visibility = View.VISIBLE
        layNoDataMyNews.visibility = View.GONE
        rvMyNews.adapter = newsAdapter
        dismissProgress()

    }

    private fun deleteNews(item: News) {
        showProgress()
        ApiClient.get().create(ApiInterface::class.java)
            .deleteNews(item.id)
            .enqueue(object : Callback<CommonResponse> {
                override fun onResponse(call: Call<CommonResponse>?, response: Response<CommonResponse>?) {
                    if (response!!.code() == 200) {
                        runOnUiThread {
                            dismissProgress()
                            news.remove(item)
                            rvMyNews.adapter?.notifyDataSetChanged()
                            if (response.body()!!.message == "deleted") {
                                showSuccess(getString(R.string.msg_delete_news))
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

    private fun showDeleteAlert(item: News) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.msg_alert))
        builder.setMessage(getString(R.string.lbl_delete_news))
        builder.setPositiveButton(getString(R.string.msg_yes)) { dialog, which ->
            dialog.cancel()
            deleteNews(item)
        }
        builder.setNegativeButton(getString(R.string.msg_no)) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun waitForInternetConnection() {

        InternetUtil.observe(this, Observer { status ->
            if (status!!) {
                nvMyNews.visibility = View.VISIBLE
                layNoInternetMyNews.visibility = View.GONE
                layNoDataMyNews.visibility = View.GONE

            } else {
                layNoInternetMyNews.visibility = View.VISIBLE
                nvMyNews.visibility = View.GONE
                layNoDataMyNews.visibility = View.GONE

            }
        })
    }
}

