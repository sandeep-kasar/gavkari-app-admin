package com.gavkariadmin.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gavkariadmin.Model.ApiResponse
import com.gavkariadmin.Model.SignInBody
import com.gavkariadmin.Model.SignInResponse
import com.gavkariadmin.repository.LoginRepository


class LoginViewModel : ViewModel() {

    private val loginRepository = LoginRepository()

    /**
     * user login  api call
     */
    fun signInCall(userBody: SignInBody): LiveData<ApiResponse<SignInResponse, String>> {
        return loginRepository.signInUser(userBody)
    }

}
