package com.ricardomorarey.ninetynine.ninetyninemobilechallenge.Api

import com.ricardomorarey.ninetynine.ninetyninemobilechallenge.models.Companies
import com.ricardomorarey.ninetynine.ninetyninemobilechallenge.models.CompaniesDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CompaniesService {

    @GET("companies")
    fun getCompaniesList(): Call<List<Companies>>

    @GET("companies/{companyId}")
    fun getCompaniesDetails(@Path("companyId") companyId: Int): Call<CompaniesDetails>

}