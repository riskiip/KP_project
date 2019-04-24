package com.rizki.inventarisperangkat.Retrofit;

import com.google.gson.JsonObject;
import com.rizki.inventarisperangkat.Value;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {

    @Headers("Content-Type: application/json")

    @FormUrlEncoded
    @POST("create_data.php")
    Call<JsonObject> createData(
            @Field("jenis") String jenis,
            @Field("no_inv") String no_inv,
            @Field("merk") String merk,
            @Field("th_dipa") String th_dipa,
            @Field("lok_no_ruang") String lok_no_ruang,
            @Field("lok_lantai") String lok_lantai,
            @Field("lok_gedung") String lok_gedung,
            @Field("lok_kawasan") String lok_kawasan,
            @Field("status") String status,
            @Field("keterangan") String keterangan,
            @Field("foto") String foto,
            @Field("lokasi_maps") String lokasi_maps

    );

    @FormUrlEncoded
    @POST("update_data.php")
    Call<Value> ubah (
            @Field("id") int id,
            @Field("status") String status,
            @Field("lok_no_ruang") String lok_no_ruang,
            @Field("lok_lantai") String lok_lantai,
            @Field("lok_gedung") String lok_gedung,
            @Field("th_dipa") String th_dipa,
            @Field("keterangan") String keterangan
    );

    @FormUrlEncoded
    @POST("delete_data.php")
    Call<Value> hapus (@Field("id") int id);
}
