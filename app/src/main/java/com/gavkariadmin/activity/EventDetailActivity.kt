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
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import com.gavkariadmin.BuildConfig
import com.gavkariadmin.R
import com.gavkariadmin.adapter.MainSliderAdapter
import com.gavkariadmin.constant.HttpConstant
import com.gavkariadmin.constant.HttpConstant.BASE_EVENT_DOWNLOAD_URL
import com.gavkariadmin.service.GlideImageLoadingService
import com.gavkariadmin.Model.AllEvent
import com.gavkariadmin.Model.Design
import com.gavkariadmin.Model.EventMediaResp
import com.gavkariadmin.constant.AppConstant
import com.gavkariadmin.constant.AppConstant.BASIC
import com.gavkariadmin.constant.AppConstant.DASHKRIYA_VIDHI
import com.gavkariadmin.constant.AppConstant.ENGAGEMENT
import com.gavkariadmin.constant.AppConstant.FIRST_MEMORIAL
import com.gavkariadmin.constant.AppConstant.HOUSE_WARMING
import com.gavkariadmin.constant.AppConstant.JAGARAN_GONDHAL
import com.gavkariadmin.constant.AppConstant.PUBLISHED
import com.gavkariadmin.constant.AppConstant.WEDDING
import com.gavkariadmin.network.ApiClient
import com.gavkariadmin.network.ApiInterface
import com.gavkariadmin.utility.Util
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.AppBarLayout
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.layout_content_main_event.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ss.com.bannerslider.Slider
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference


class EventDetailActivity : BaseActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,View.OnClickListener {

    private lateinit var bundleData: Any

    private lateinit var mMap: GoogleMap

    private lateinit var allEvent: AllEvent

    private var eventId: String = "-1"

    private var latitude: Double = 0.0

    private var longitude: Double = 0.0

    private var address: String = ""

    private var photo = ""

    private var Title = ""

    private var SubTitle = ""

    private var EventDate = ""

    private var type = 0

    private var status = 0

    private lateinit var design: Design

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        Slider.init(GlideImageLoadingService(applicationContext))
        setupViews()

    }

    private fun setupViews() {

        setupToolbar(R.id.toolbar, getString(R.string.title_event_details))
        initCollapsingToolbar()
        bundleData = intent.getSerializableExtra("VillageResponse")
        setData()

        tvContactNo.setOnClickListener(this)
        tvViewPatrika.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            tvContactNo ->{call()}

            tvViewPatrika->{
                startActivity(Intent(this, SingleDesignActivity::class.java)
                    .putExtra("event_obj", design))

            }
        }
    }

    private fun call(){
        var mobileNos= tvContactNo.text.toString().trim()
        val mobile = mobileNos.substring(0,Math.min(mobileNos.length,10))
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile))
        startActivity(intent)
    }

    private fun getEventMedia(eventId:String) {

        eventId?.let {
            ApiClient.get().create(ApiInterface::class.java)
                .getEventMedia(it)
                .enqueue(object : Callback<EventMediaResp> {
                    override fun onResponse(call: Call<EventMediaResp>?, response: Response<EventMediaResp>?) {

                        if (response!!.code() == 200) {
                            var response = response!!.body()
                            if (response?.status == HttpConstant.SUCCESS) {
                                runOnUiThread {
                                    var imageList= response.photos
                                    slider.setAdapter(MainSliderAdapter(imageList,BASE_EVENT_DOWNLOAD_URL))
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
                    collapsing_toolbar.title = getString(R.string.title_event_details)
                    isShow = true
                } else if (isShow) {
                    collapsing_toolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun setData() {

        if (bundleData is AllEvent) {

            allEvent = bundleData as AllEvent

            if(allEvent.status == PUBLISHED){
                tvViewPatrika.visibility = View.VISIBLE
                if (allEvent.type == AppConstant.BIRTHDAY ||allEvent.type == AppConstant.RETIREMENT ||allEvent.type == AppConstant.SATYANARAYAN_POOJA ||
                    allEvent.type == AppConstant.MAHAPRASAD ||allEvent.type == AppConstant.OTHER_EVENT){
                    tvViewPatrika.visibility = View.GONE
                }else{
                    tvViewPatrika.visibility = View.VISIBLE
                }
            }else{
                tvViewPatrika.visibility = View.GONE
            }

            EventDate = Util.getFormatedDate(allEvent.event_date,
                "yyyy-MM-dd HH:mm:ss", "EEEE, MMMM d, HH:mm",resources).toString()
            tvEventType.text = allEvent?.title
            tvFamily.text = allEvent?.family
            tvDateTime.text = allEvent?.muhurt
            tvAddress.text = allEvent?.address
            tvContactNo.text = allEvent?.contact_no
            tvTip.text = allEvent?.note

            if(allEvent.type== WEDDING || allEvent.type== ENGAGEMENT){
                var subtitle = allEvent.subtitle +" "+ allEvent.subtitle_one +" "+getString(R.string.lbl_and)+" "+
                        allEvent.subtitle_three +" "+allEvent.subtitle_four +" "+getString(R.string.lbl_there_wedding)
                SubTitle =  subtitle
                tvEventSubTitle.text = subtitle
            }else{
                SubTitle =  allEvent.subtitle
                tvEventSubTitle.text = allEvent.subtitle
            }

            if(allEvent.type == WEDDING || allEvent.type == ENGAGEMENT ){
                tvRelInfo.text = getString(R.string.nimantrak)
            }


            if(allEvent.type == DASHKRIYA_VIDHI || allEvent.type == FIRST_MEMORIAL ){
                tvRelInfo.text = getString(R.string.vinit)
            }

            if(allEvent.type == JAGARAN_GONDHAL || allEvent.type == HOUSE_WARMING ){
                tvRelInfo.text = getString(R.string.snehankit)
            }

            tvRelInfoDetails.text = allEvent.description

            tvMoreInfo.text = allEvent.description_one

            latitude = allEvent.latitude.toDouble()

            longitude = allEvent.longitude.toDouble()

            address = allEvent.address

            photo = allEvent.photo

            Title = allEvent.title

            SubTitle = allEvent.subtitle

            eventId=allEvent.id

            type  = allEvent.type

            status = allEvent.status.toInt()

            getEventMedia(eventId)

            design = Design(type,Title,allEvent.family,allEvent.muhurt,SubTitle,
                allEvent.subtitle_one,allEvent.subtitle_two,allEvent.subtitle_three,
                allEvent.subtitle_four,allEvent.subtitle_five,allEvent.note,
                allEvent.description,allEvent.description_one,allEvent.address,allEvent.photo, BASIC)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {

        //init map object
        mMap = googleMap

        // Add a marker and zoom the camera.
        val addressLocal = LatLng(latitude, longitude)
        val cameraPosition = CameraPosition.Builder().target(addressLocal).zoom(15f).build()
        mMap.addMarker(MarkerOptions().position(addressLocal)
            .title(address))
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        mMap.uiSettings.isMapToolbarEnabled = true
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return true
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
                if (status == PUBLISHED){
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
                        MyAsyncTask(this@EventDetailActivity).execute()
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permenantly, navigate user to app settings
                        Util.showPermissionAlert(this@EventDetailActivity!!,
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
        class MyAsyncTask internal constructor(context: EventDetailActivity) : AsyncTask<Int, String, String?>() {

            private val activityReference: WeakReference<EventDetailActivity> = WeakReference(context)
            val activity = activityReference.get()
            override fun onPreExecute() {
                if (activity == null || activity.isFinishing) return
            }


            @SuppressLint("WrongThread")
            override fun doInBackground(vararg params: Int?): String? {

                var content = activity?.findViewById<CoordinatorLayout>(R.id.layScreen)
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
                share.putExtra(Intent.EXTRA_TEXT,
                    activity?.getString(R.string.msg_share_more_info) + "\n" + activity?.getString(R.string.url_gavkari_app_play_store));
                share.type = "image/*"
                activity?.startActivity(Intent.createChooser(share, activity?.getString(R.string.tittle_share_via)))

                return "done"
            }


            override fun onPostExecute(result: String?) {
                if (activity == null || activity.isFinishing) return
            }

        }
    }

}
