package com.rizki.inventarisperangkat.AddPerangkat;

import android.util.Log;

import com.google.gson.JsonObject;
import com.rizki.inventarisperangkat.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPresenter {
    private AddView addView;

    public AddPresenter(AddView addView){
        this.addView = addView;
    }

    public void addPerangkat(){
        final String tag = "Add-addPerangkat";
        String jenis = addView.getJenis();
        String no_inv = addView.getNo_inv();
        String merk = addView.getMerk();
        String th_dipa = addView.getTh_Dipa();
        String lok_no_ruang = addView.getLok_no_ruang();
        String lok_lantai = addView.getLok_lantai();
        String lok_gedung = addView.getLok_gedung();
        String lok_kawasan = addView.getLok_kawasan();
        String status = addView.getStatus();
        String keterangan = addView.getKeterangan();
        String foto = addView.getFoto();
        String lok_maps = addView.getLokasi_maps();

        RetrofitClient.getInstance()
                .getApi()
                .createData(jenis, no_inv, merk, th_dipa, lok_no_ruang, lok_lantai, lok_gedung, lok_kawasan, status, keterangan, foto, lok_maps)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            addView.successAddPerangkat();
                            Log.e(tag, response.body().toString());
                        } else {
                            addView.failedAddPerangkat();
                            Log.e(tag, response.body().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        addView.failedAddPerangkat();
                        Log.e(tag, t.getMessage().toString());
                    }
                });
    }
}
