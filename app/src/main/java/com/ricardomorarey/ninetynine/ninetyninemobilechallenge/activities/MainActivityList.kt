package com.ricardomorarey.ninetynine.ninetyninemobilechallenge.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.ricardomorarey.ninetynine.ninetyninemobilechallenge.Api.CompaniesService
import com.ricardomorarey.ninetynine.ninetyninemobilechallenge.R
import com.ricardomorarey.ninetynine.ninetyninemobilechallenge.adapters.CompaniesAdapater
import com.ricardomorarey.ninetynine.ninetyninemobilechallenge.models.Companies
import kotlinx.android.synthetic.main.activity_main_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivityList : AppCompatActivity() {

    lateinit var service: CompaniesService
    val TAG_LOGS = "yooo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list)

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://dev.ninetynine.com/testapi/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create<CompaniesService>(CompaniesService::class.java)

        getCompaniesList()

        main_list.setOnItemClickListener { adapterView, view, position, l ->
            val company = main_list.getItemAtPosition(position) as Companies
            val requestCompanyId = company.id
            Toast.makeText(this, "${company.id}",Toast.LENGTH_LONG).show()
            val intent = Intent(this, DetailsCompanyActivity::class.java)
            intent.putExtra("requestCompanyId", requestCompanyId)
            startActivity(intent)

        }

    }

    fun getCompaniesList(){
        service.getCompaniesList().enqueue(object: Callback<List<Companies>>{
            override fun onResponse(call: Call<List<Companies>>?, response: Response<List<Companies>>?) {
                val posts = response?.body()!!
                Log.i(TAG_LOGS, Gson().toJson(posts))
                lateinit var adapter: CompaniesAdapater
                lateinit var companiesList: List<Companies>
                companiesList = posts
                adapter = CompaniesAdapater( applicationContext, R.layout.list_view_companies, companiesList)
                main_list.adapter = adapter
            }
            override fun onFailure(call: Call<List<Companies>>?, t: Throwable?) {
                t?.printStackTrace()
            }
        })
    }
}
