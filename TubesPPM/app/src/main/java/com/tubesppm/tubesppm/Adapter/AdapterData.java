package com.tubesppm.tubesppm.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tubesppm.tubesppm.API.APIRequestData;
import com.tubesppm.tubesppm.API.RetroServer;
import com.tubesppm.tubesppm.Activity.MainActivity;
import com.tubesppm.tubesppm.Activity.UbahActivity;
import com.tubesppm.tubesppm.Model.DataModel;
import com.tubesppm.tubesppm.Model.ResponseModel;
import com.tubesppm.tubesppm.R;

import java.util.List;
import android.os.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData> {

    private Context ctx;
    private List<DataModel> listData;
    private List<DataModel> listLaundry;
    private int idPa;

    public AdapterData(Context ctx, List<DataModel> listData) {
        this.ctx = ctx;
        this.listData = listData;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModel dm = listData.get(position);

        holder.vId.setText(String.valueOf(dm.getId()));
        holder.vNama.setText(dm.getNama());
        holder.vJurusan.setText(dm.getJurusan());
        holder.vJudul.setText(dm.getJudul());
    }

    @Override
    public int getItemCount() { return listData.size(); };

    public class HolderData extends RecyclerView.ViewHolder {
        TextView vId, vNama, vJurusan, vJudul;

        public HolderData(@NonNull View itemView)  {
            super(itemView);

            vId = itemView.findViewById(R.id.v_id);
            vNama = itemView.findViewById(R.id.v_nama);
            vJurusan = itemView.findViewById(R.id.v_jurusan);
            vJudul = itemView.findViewById(R.id.v_judul);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder dialogPesan = new AlertDialog.Builder(ctx);
                    dialogPesan.setIcon(R.mipmap.ic_launcher_round);
                    dialogPesan.setCancelable(true);

                    idPa = Integer.parseInt(vId.getText().toString());

                    dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteData();
                            dialogInterface.dismiss();
                            Handler hand = new Handler();
                            hand.postDelayed(new Runnable() {
                                @Override
                                public void run() {((MainActivity) ctx).retrieveData(); }
                            }, 1000);
                        }
                    });

                    dialogPesan.setNegativeButton("Ubah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getData();
                            dialogInterface.dismiss();
                        }
                    });

                    dialogPesan.show();

                    return false;
                }
            });
        }

        private void deleteData(){
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> hapusData = ardData.ardDeleteData(idPa);

            hapusData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    Toast.makeText(ctx, "Pesan: " + pesan, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal menghubungi server :" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void getData(){
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> ambilData = ardData.ardGetData(idPa);

            ambilData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    listLaundry = response.body().getData();

                    int varIdPa = listLaundry.get(0).getId();
                    String varNama = listLaundry.get(0).getNama();
                    String varJurusan = listLaundry.get(0).getJurusan();
                    String varJudul = listLaundry.get(0).getJudul();

                    Intent kirim = new Intent(ctx, UbahActivity.class);
                    kirim.putExtra("jId", varIdPa);
                    kirim.putExtra("jNama", varNama);
                    kirim.putExtra("jJurusan", varJurusan);
                    kirim.putExtra("jJudul", varJudul);
                    ctx.startActivity(kirim);

                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx,"Gagal Menghubungi Server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


}
