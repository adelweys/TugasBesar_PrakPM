package com.tubesppm.tubesppm.Activity

import android.content.Intent
import android.graphics.SweepGradient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
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


class TambahActivity : AppCompatActivity() {

    private var btnSimpan: Button? = null
    private var nama: String? = null
    private var jurusan: String? = null
    private var judul: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah)
        var jNama = findViewById<TextView>(R.id.nama)
        var jJurusan = findViewById<TextView>(R.id.jurusan)
        var jJudul = findViewById<TextView>(R.id.judul)

        btnSimpan = findViewById(R.id.btn_simpan)
        btnSimpan?.run {
            setOnClickListener {
                nama = jNama.text.toString()
                jurusan = jJurusan.text.toString()
                judul = jJudul.text.toString()
                if (nama!!.trim { it <= ' ' } == "") {
                    jNama.error = "Masukkan Nama!"
                } else if (jurusan!!.trim { it <= ' ' } == "") {
                    jJurusan.error = "Masukkan Jurusan!"
                } else if (judul!!.trim { it <= ' ' } == "") {
                    jJudul.error = "Masukkan Judul!"
                } else {
                    createData();
                }
            }
        }
    }

    public fun createData() {
        val ardData: APIRequestData = RetroServer.konekRetrofit().create(APIRequestData::class.java)
        val simpanData: Call<ResponseModel> = ardData.ardCreateData(nama, jurusan, judul)
        simpanData.enqueue(object : Callback<ResponseModel>{
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val pesan: String = response.body()!!.pesan
                Toast.makeText(
                    this@TambahActivity,
                    "Pesan: $pesan",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }

            override fun onFailure(call: Call<ResponseModel>, m: Throwable){
                Toast.makeText(
                    this@TambahActivity,
                    "Gagal terkoneksi | " + m.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


}

