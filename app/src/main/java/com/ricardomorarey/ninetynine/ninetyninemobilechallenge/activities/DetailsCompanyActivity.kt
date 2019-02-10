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
import com.ricardomorarey.ninetynine.ninetyninemobilechallenge.models.CompaniesDetails
import kotlinx.android.synthetic.main.activity_details_company.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailsCompanyActivity : AppCompatActivity() {

    lateinit var service: CompaniesService
    lateinit var job: Job

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_company)

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

        //Recupero el id del seleccionado para poder montar la cadena de conexion de peticion de detalles
        val requestCompanyId: Int = intent.getIntExtra("requestCompanyId", -1)

        getCompaniesDetails(requestCompanyId)

        job = GlobalScope.launch {
            repeat(100) { i ->
                getCompaniesDetails(requestCompanyId)
                delay(20000L)
            }
        }

        //Creo un  buttonBack para volver a la principal y paro las  peteciones de detalles cada 20 seg
        buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivityList::class.java)
            startActivity(intent)
            job.cancel()
            finish()
        }
    }

    //sobreescribo este metodo para parar las petciones cada 20 seg si vuelven con el back
    override fun onBackPressed() {
        val intent = Intent(this, MainActivityList::class.java)
        startActivity(intent)
        job.cancel()
        super.onBackPressed()
        finish()
    }


    //metodo para obtener los detalles de cada empresa y pintarlos directamente
    fun getCompaniesDetails(requestCompanyId: Int){

        var post: CompaniesDetails? = null
        service.getCompaniesDetails(requestCompanyId).enqueue(object: Callback<CompaniesDetails>{
            override fun onResponse(call: Call<CompaniesDetails>?, response: Response<CompaniesDetails>?) {
                post = response?.body()
                if (post != null) {
                    textView_detail_name.text = post?.name
                    textView_detail_ric.text = post?.ric
                    textView_detail_shareprice.text = post?.sharePrice.toString()
                    textView_detail_description.text = post?.description
                    textView_detail_country.text = post?.country
                } else {
                    Toast.makeText(this@DetailsCompanyActivity
                        , "There was a connection problem with the server, try it later",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<CompaniesDetails>?, t: Throwable?) {
                t?.printStackTrace()
            }
        })
    }

}

