package com.gavkariadmin.activity

import android.app.Activity
import android.app.Dialog
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.gavkariadmin.interfaces.AlertMessageCallback
import com.gavkariadmin.interfaces.BasicBehaviourProvider
import com.gavkariadmin.utility.DiaplayDialog
import com.gavkariadmin.utility.DisplayMessage


open class BaseActivity : AppCompatActivity(), BasicBehaviourProvider {


    private lateinit var dialog: Dialog


    override fun showError(message: String) {
        DisplayMessage.DisplayError(message, this)

    }

    override fun showWarning(message: String) {
        DisplayMessage.DisplayWarning(message, this)

    }

    override fun showSuccess(message: String) {
        DisplayMessage.DisplaySuccess(message, this)
    }

    override fun showCustomMessage(message: String) {

    }

    override fun showProgress() {
        dialog = Dialog(this)
        if (dialog != null) {
            DiaplayDialog.progressDialog(dialog)
        }
    }

    override fun dismissProgress() {
        if (dialog != null) {
            dialog.dismiss()
        }

    }

    override fun showAlert(activity: Activity, alertMessageCallback: AlertMessageCallback,
                           setTitle: String, setMessage: String, setPositiveButton: String,
                           setNegativeButton: String) {
        DiaplayDialog.AlertDialog(activity, alertMessageCallback, setTitle, setMessage,
                setPositiveButton, setNegativeButton)
    }


    fun setupToolbar(toolbarId: Int, title: String) {
        val myToolbar = findViewById<View>(toolbarId) as Toolbar
        setSupportActionBar(myToolbar)
        if (supportActionBar != null) {
            supportActionBar?.title = title
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    }

    override fun waitForInternet() {
    }

}
