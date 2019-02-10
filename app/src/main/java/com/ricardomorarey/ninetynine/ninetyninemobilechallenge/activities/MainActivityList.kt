package com.ricardomorarey.ninetynine.ninetyninemobilechallenge.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ricardomorarey.ninetynine.ninetyninemobilechallenge.Api.CompaniesService
import com.ricardomorarey.ninetynine.ninetyninemobilechallenge.R
import com.ricardomorarey.ninetynine.ninetyninemobilechallenge.adapters.CompaniesAdapater
import com.ricardomorarey.ninetynine.ninetyninemobilechallenge.models.Companies
import kotlinx.android.synthetic.main.activity_main_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivityList : AppCompatActivity() {

    lateinit var service: CompaniesService
    lateinit var jobMain: Job
    var backButtonCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ricardomorarey.ninetynine.ninetyninemobilechallenge.R.layout.activity_main_list)

        // Libreria retrofit
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://dev.ninetynine.com/testapi/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create<CompaniesService>(CompaniesService::class.java)

        //Compruebo que hay conexión si no la hay le muestro un toast al usuario
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (!(networkInfo != null && networkInfo.isConnected)) Toast.makeText(this,
            "There was a connection problem with the server, try it later", Toast.LENGTH_LONG).show()

        //Creo un corutine de Kotlin para referescar cada 20 segundos la lista, pero la limito a 100 repeticiones (33 minutos)
        jobMain = GlobalScope.launch {
            repeat(100) { i ->
                getCompaniesList()
                delay(20000L)
            }
        }

        // en el onsetonclicklistener cancelo la corutine para que no sigua haciendo peticiones, cojo la posiico y voy a la vista de detalle
        main_list.setOnItemClickListener { adapterView, view, position, l ->
            val company = main_list.getItemAtPosition(position) as Companies
            val requestCompanyId = company.id
            jobMain.cancel()
            val intent = Intent(this, DetailsCompanyActivity::class.java)
            intent.putExtra("requestCompanyId", requestCompanyId)
            startActivity(intent)
            finish()
        }

    }

    //Creo la funcion para pedir la lista de emprresas y en ella cargao el daptador personalizado de mi lista
    fun getCompaniesList(){
        service.getCompaniesList().enqueue(object: Callback<List<Companies>>{
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<List<Companies>>?, response: Response<List<Companies>>?) {
                val posts = response?.body()!!
                lateinit var adapter: CompaniesAdapater
                lateinit var companiesList: List<Companies>
                companiesList = posts
                val sortedList = companiesList.sortedWith(compareBy(Companies::sharePrice).reversed())
                adapter = CompaniesAdapater( applicationContext, R.layout.list_view_companies, sortedList)
                main_list.adapter = adapter
            }
            override fun onFailure(call: Call<List<Companies>>?, t: Throwable?) {
                t?.printStackTrace()
            }
        })
    }

    //sobreescribo el buttonBack para que se comporte según quiero
    override fun onBackPressed() {
        if (isTaskRoot) {
            if (backButtonCount >= 1) {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            } else {
                Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_LONG).show()
                backButtonCount++
            }
        } else {
            super.onBackPressed()
        }
    }
}

