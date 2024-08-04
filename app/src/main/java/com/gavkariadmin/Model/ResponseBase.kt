package com.gavkariadmin.Model


data class ApiResponse<R, E>(var response: R?, var error: E?)