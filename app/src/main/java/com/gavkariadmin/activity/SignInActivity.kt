package com.gavkariadmin.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.CallbackManager
import com.gavkariadmin.Model.ApiResponse
import com.gavkariadmin.R
import com.gavkariadmin.Model.SignInBody
import com.gavkariadmin.Model.SignInResponse
import com.gavkariadmin.utility.InputValidatorHelper
import com.gavkariadmin.utility.InternetUtil
import com.gavkariadmin.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity(), View.OnClickListener {

    private lateinit var loginViewmodel: LoginViewModel

    private lateinit var usersignInobserver: Observer<ApiResponse<SignInResponse, String>>

    private lateinit var mCallbackManager: CallbackManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //init view model
        loginViewmodel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        //init facebook callback manager
        mCallbackManager = CallbackManager.Factory.create()

        //init observer
        initSigninObserver()

        //click
        btnSignIn.setOnClickListener(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
    }


    override fun onClick(v: View?) {

        when (v) {

            btnSignIn -> signIn()

        }
    }

    private fun signIn() {



        //take params
        val name = edtAdminName.text.toString().trim()
        val password = edtPassword.text.toString()

        //validate the input params
        val isValidInput: Boolean = validate(name, password)

        //if input is valid then call register api
        if (isValidInput) {

            //create user body
            val userBody = SignInBody(name, password)

            //login progress
            showProgress()

            if (InternetUtil.isInternetOn()) {
                //pass data to view model
                loginViewmodel.signInCall(userBody).observe(this, usersignInobserver)
            } else {
                //observe internet connection
                dismissProgress()
                waitForInternet()
            }
        }

    }

    private fun initSigninObserver() {

        usersignInobserver = Observer {

            dismissProgress()

            if (it?.response != null) {

                var userSignInResponse = it.response

                if (userSignInResponse?.status == 1) {
                    startActivity(Intent(this,HomeActivity::class.java))
                    finish()
                } else {
                    if (userSignInResponse!!.message =="User dose not exist !"){
                        showSuccess(getString(R.string.msg_no_user))
                    }else{
                        showError(userSignInResponse.message)
                    }
                }

            } else {
                showError(it?.error!!)
            }
        }
    }

    private fun validate(mobile: String, password: String): Boolean {


        if (InputValidatorHelper.isNullOrEmpty(mobile)) {

            showError(getString(R.string.warning_empty_name))

            return false

        } else if (InputValidatorHelper.isNullOrEmpty(password)) {

            showError(getString(R.string.warning_empty_password))

            return false

        } else if (InputValidatorHelper.isValidPassword(password)) {

            showError(getString(R.string.warning_invalid_password))

            return false

        } else {

            return true
        }

    }

}
