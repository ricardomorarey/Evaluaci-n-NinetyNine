package com.ricardomorarey.ninetynine.ninetyninemobilechallenge.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.Gson
import com.ricardomorarey.ninetynine.ninetyninemobilechallenge.Api.CompaniesService
import com.ricardomorarey.ninetynine.ninetyninemobilechallenge.R
import com.ricardomorarey.ninetynine.ninetyninemobilechallenge.models.CompaniesDetails
import kotlinx.android.synthetic.main.activity_details_company.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.concurrent.timer

class DetailsCompanyActivity : AppCompatActivity() {

    lateinit var service: CompaniesService
    val TAG_LOGS = "yooo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_company)

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://dev.ninetynine.com/testapi/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create<CompaniesService>(CompaniesService::class.java)

        val requestCompanyId: Int = intent.getIntExtra("requestCompanyId", -1)

        getCompaniesDetails(requestCompanyId)

    }

    fun getCompaniesDetails(requestCompanyId: Int){
        //Recibimos los datos del post con ID = 1
        var post: CompaniesDetails? = null
        service.getCompaniesDetails(requestCompanyId).enqueue(object: Callback<CompaniesDetails>{
            override fun onResponse(call: Call<CompaniesDetails>?, response: Response<CompaniesDetails>?) {
                post = response?.body()
                Log.i(TAG_LOGS, Gson().toJson(post))
                textView_detail_name.text = post?.name
                textView_detail_ric.text = post?.ric
                textView_detail_shareprice.text = post?.sharePrice.toString()
                textView_detail_description.text = post?.description
                textView_detail_country.text = post?.country
            }
            override fun onFailure(call: Call<CompaniesDetails>?, t: Throwable?) {
                t?.printStackTrace()
            }
        })
    }

}

