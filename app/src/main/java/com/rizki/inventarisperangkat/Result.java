package com.rizki.inventarisperangkat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("jenis")
    @Expose
    private String jenis;
    @SerializedName("no_inv")
    @Expose
    private String no_inv;
    @SerializedName("merk")
    @Expose
    private String merk;
    @SerializedName("th_dipa")
    @Expose
    private String th_dipa;
    @SerializedName("lok_no_ruang")
    @Expose
    private String lok_no_ruang;
    @SerializedName("lok_lantai")
    @Expose
    private String lok_lantai;
    @SerializedName("lok_gedung")
    @Expose
    private String lok_gedung;
    @SerializedName("lok_kawasan")
    @Expose
    private String lok_kawasan;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;
    @SerializedName("foto")
    @Expose
    private String foto;
    @SerializedName("lokasi_maps")
    @Expose
    private String lokasi_maps;

    public String getId() {
        return id;
    }

    public String getJenis() {
        return jenis;
    }

    public String getNo_inv() {
        return no_inv;
    }

    public String getMerk() {
        return merk;
    }

    public String getTh_dipa() {
        return th_dipa;
    }

    public String getLok_no_ruang() {
        return lok_no_ruang;
    }

    public String getLok_lantai() {
        return lok_lantai;
    }

    public String getLok_gedung() {
        return lok_gedung;
    }

    public String getLok_kawasan() {
        return lok_kawasan;
    }

    public String getStatus() {
        return status;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getFoto() {
        return foto;
    }

    public String getLokasi_maps() {
        return lokasi_maps;
    }
}
