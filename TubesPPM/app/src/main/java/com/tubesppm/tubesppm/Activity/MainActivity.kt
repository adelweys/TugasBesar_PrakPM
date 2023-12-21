package com.tubesppm.tubesppm.Activity

import android.content.Intent
import android.graphics.SweepGradient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tubesppm.tubesppm.API.APIRequestData
import com.tubesppm.tubesppm.API.RetroServer
import com.tubesppm.tubesppm.Adapter.AdapterData
import com.tubesppm.tubesppm.Model.DataModel
import com.tubesppm.tubesppm.Model.ResponseModel
import com.tubesppm.tubesppm.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class MainActivity : AppCompatActivity() {
    var rvData: RecyclerView? = null;
    var adData: RecyclerView.Adapter<*>? = null;
    var lmData: RecyclerView.LayoutManager? = null;
    var listData: List<DataModel> = ArrayList<DataModel>()
    var srlData: SwipeRefreshLayout? = null;
    var pbData: ProgressBar? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvData = findViewById(R.id.rv_data)
        srlData = findViewById(R.id.srl_data)
        pbData = findViewById(R.id.pb_data)
        var btnTambah = findViewById<FloatingActionButton>(R.id.btn_tambah)

        lmData = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        with(rvData) { this?.setLayoutManager(lmData) }

        //retrieveData

        with(srlData){
            this?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
                setRefreshing(true)
                retrieveData()
                setRefreshing(false)
            })
        }

        btnTambah.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    TambahActivity::class.java
                )
            )
        })
    }

    override fun onResume() {
        super.onResume()
        retrieveData()
    }

    fun retrieveData(){
        pbData!!.visibility = View.VISIBLE

        val ardData: APIRequestData = RetroServer.konekRetrofit().create(APIRequestData::class.java)
        val tampilData: Call<ResponseModel> = ardData.ardRetrieveData()

        tampilData.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                listData = response.body()!!.getData()
                adData = AdapterData(this@MainActivity, listData)
                rvData!!.adapter = adData
                adData!!.notifyDataSetChanged()
                pbData!!.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Gagal terkoneksi: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
                pbData!!.visibility = View.INVISIBLE
            }
        })

    }



}