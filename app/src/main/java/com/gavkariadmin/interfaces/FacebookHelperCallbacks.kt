package com.gavkariadmin.interfaces

import com.facebook.login.LoginResult

/**
 * Created by rekha on 16/3/18.
 */
interface FacebookHelperCallbacks {

    fun onFacebookSignedInFinished(loginResult: LoginResult)
    fun onFacebookCancel(message: String)
    fun onFacebookError(message: String)
}