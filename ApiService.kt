package com.watuke.watu.screens

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("fetch_provisioning_data.php")
    fun getProvisioningData(): Call<ProvisioningData>
}