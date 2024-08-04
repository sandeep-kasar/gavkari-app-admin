package com.gavkariadmin.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.FileProvider
import com.gavkariadmin.BuildConfig
import com.gavkariadmin.R
import com.gavkariadmin.Model.EventMediaResp
import com.gavkariadmin.Model.News
import com.gavkariadmin.adapter.MainSliderAdapter
import com.gavkariadmin.constant.AppConstant
import com.gavkariadmin.constant.HttpConstant
import com.gavkariadmin.network.ApiClient
import com.gavkariadmin.network.ApiInterface
import com.gavkariadmin.service.GlideImageLoadingService
import com.gavkariadmin.utility.Util
import com.google.android.material.appbar.AppBarLayout
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_news_detail.*
import kotlinx.android.synthetic.main.layout_containt_main_news.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ss.com.bannerslider.Slider
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference

class NewsDetailActivity : BaseActivity(){

    private lateinit var news: News

    private var Title = ""

    private var status = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        Slider.init(GlideImageLoadingService(applicationContext))
        setupViews()

    }

    private fun setupViews() {

        setupToolbar(R.id.toolbar, getString(R.string.title_news_detail))
        initCollapsingToolbar()
        news = intent.getSerializableExtra("news_data") as News
        setData()
    }

    private fun initCollapsingToolbar() {

        collapsing_toolbar.title = " "
        appbar.setExpanded(true)

        // hiding & showing the title when toolbar expanded & collapsed
        appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var isShow = false
            internal var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsing_toolbar.title = getString(R.string.title_news_detail)
                    isShow = true
                } else if (isShow) {
                    collapsing_toolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun setData() {

        if (news.news_type == AppConstant.MP || news.news_type == AppConstant.MLA){
            tvPlace.text = getString(R.string.lbl_political)
        }else if(news.news_type == AppConstant.AGRICULTURAL || news.news_type == AppConstant.GOVT_SCHEME){
            tvPlace.text = getString(R.string.lbl_public)
        }else{
            tvPlace.text = getString(R.string.lbl_gav)
        }

        tvtitle.text = news.title.trim()
        tvSource.text = "( "+ news.source.trim() + " )"
        tvShortDecription.text = news.short_description.trim()
        tvDescription.text = news.description

        var inputDate = Util.getFormatedDate(news.news_date,
            "yyyy-MM-dd", "EEEE, d MMMM",resources)

        tvDate.text = inputDate

        getNewsMedia(news.id)
    }

    private fun getNewsMedia(eventId:String) {

        eventId?.let {
            ApiClient.get().create(ApiInterface::class.java)
                .getNewsMedia(it)
                .enqueue(object : Callback<EventMediaResp> {
                    override fun onResponse(call: Call<EventMediaResp>?, response: Response<EventMediaResp>?) {
                        if (response!!.code() == 200) {
                            var response = response!!.body()
                            runOnUiThread {
                                if (response?.status == HttpConstant.SUCCESS) {
                                    var imageList= response.photos
                                    slider.setAdapter(MainSliderAdapter(imageList,HttpConstant.BASE_NEWS_DOWNLOAD_URL))
                                }
                            }


                        }
                    }

                    override fun onFailure(call: Call<EventMediaResp>?, t: Throwable?) {
                        runOnUiThread {
                            showError(getString(R.string.warning_try_later))
                        }
                    }
                })
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.event_details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {


        return when (item?.itemId) {
            R.id.action_share_ -> {
                if (status == AppConstant.PUBLISHED){
                    checkGalleryPermission()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkGalleryPermission() {

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        MyAsyncTask(this@NewsDetailActivity).execute()
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permenantly, navigate user to app settings
                        Util.showPermissionAlert(this@NewsDetailActivity!!,
                            getString(R.string.message_enable_permission), getString(R.string.app_name))
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }

    companion object {
        class MyAsyncTask internal constructor(context: NewsDetailActivity) : AsyncTask<Int, String, String?>() {

            private val activityReference: WeakReference<NewsDetailActivity> = WeakReference(context)
            val activity = activityReference.get()
            override fun onPreExecute() {
                if (activity == null || activity.isFinishing) return
                activity.showProgress()
            }

            @SuppressLint("WrongThread")
            override fun doInBackground(vararg params: Int?): String? {

                var content = activity?.findViewById<Slider>(R.id.slider)
                content?.isDrawingCacheEnabled = true
                var bitmap = content?.drawingCache
                val root = Environment.getExternalStorageDirectory()
                val cachePath = File(root.absolutePath + "/DCIM/Camera/image.jpg")

                try {
                    cachePath.createNewFile()
                    val ostream = FileOutputStream(cachePath)
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, ostream)
                    ostream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                var uri: Uri? = null
                uri = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    FileProvider.getUriForFile(activity!!.application, BuildConfig.APPLICATION_ID+".provider",
                        cachePath)
                } else {
                    Uri.fromFile(cachePath)
                }

                val share = Intent(Intent.ACTION_SEND)
                share.putExtra(Intent.EXTRA_STREAM, uri)
                share.putExtra(
                    Intent.EXTRA_TEXT,
                    activity?.Title + "\n"+
                            activity?.getString(R.string.msg_share_more_info) + "\n" + activity?.getString(R.string.url_gavkari_app_play_store))
                share.type = "image/*"
                activity?.startActivity(Intent.createChooser(share, activity?.getString(R.string.tittle_share_via)))

                return "done"
            }

            override fun onPostExecute(result: String?) {
                if (activity == null || activity.isFinishing) return
                activity.dismissProgress()
            }

        }
    }

}
