package com.rizki.inventarisperangkat.AddPerangkat;

public interface AddView {
    String getJenis();

    String getNo_inv();

    String getMerk();

    String getTh_Dipa();

    String getLok_no_ruang();

    String getLok_lantai();

    String getLok_gedung();

    String getLok_kawasan();

    String getStatus();

    String getKeterangan();

    String getFoto();

    String getLokasi_maps();

    void successAddPerangkat();

    void failedAddPerangkat();
}
