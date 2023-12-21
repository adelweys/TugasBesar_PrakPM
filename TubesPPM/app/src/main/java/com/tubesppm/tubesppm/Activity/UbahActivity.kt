package com.tubesppm.tubesppm.Activity

import android.content.Intent
import android.graphics.SweepGradient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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


class UbahActivity : AppCompatActivity() {

    var jId = 0
    var jNama: String? = null
    var jJurusan: String? = null
    var jJudul: String? = null
    var kNama: String? = null
    var kJurusan: String? = null
    var kJudul: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubah)
        val terima = intent
        Log.d("test", terima.toString())
        jId = terima.getIntExtra("jId", -1)
        jNama = terima.getStringExtra("jNama")
        jJurusan = terima.getStringExtra("jJurusan")
        jJudul = terima.getStringExtra("jJudul")

        val vNama = findViewById<TextView>(R.id.nama)
        val vJurusan = findViewById<TextView>(R.id.jurusan)
        val vJudul = findViewById<TextView>(R.id.judul)
        val btnUbah = findViewById<Button>(R.id.btn_ubah)

        vNama.setText(jNama)
        vJurusan.setText(jJurusan)
        vJudul.setText(jJudul)

        btnUbah .setOnClickListener(View.OnClickListener {
            kNama = vNama.getText().toString()
            kJurusan = vJurusan.getText().toString()
            kJudul = vJudul.getText().toString()
            updateData()
        })
    }

    private fun updateData() {
        val ardData = RetroServer.konekRetrofit().create(APIRequestData::class.java)
        val ubahData = ardData.ardUpdateData(jId, kNama, kJurusan, kJudul)
        ubahData.enqueue(object : Callback<ResponseModel>{
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val pesan = response.body()!!.pesan
                Toast.makeText(
                    this@UbahActivity,
                    "Pesan : $pesan",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(
                    this@UbahActivity,
                    "Gagal terkoneksi | " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
                }

            }

        )
    }
}
