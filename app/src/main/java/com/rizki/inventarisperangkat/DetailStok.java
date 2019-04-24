package com.rizki.inventarisperangkat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rizki.inventarisperangkat.Retrofit.API;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailStok extends AppCompatActivity {
    TextView tvDetailId, tvJenisD, tvKawasanD, tvMerkD, tvNoinvD, tvMapsD, tvlvldetail;
    EditText etStatusD, etNoruangD, etLantaiD, etGedungD, etKetD, etThdipaD;
    ImageView ivListD;
    Button btnDelete, btnUpdate;
    private static final String BASE_URL = "http://layanan.batan.go.id/mobilepjkkd/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeWidgets();
        receiveAndShowData();
        delete();
        ubah();
    }

    private void initializeWidgets(){
        tvDetailId  = findViewById(R.id.tvDetailId);
        tvJenisD    = findViewById(R.id.tvDetailJenis);
        tvKawasanD  = findViewById(R.id.tvDetailKawasan);
        tvMerkD     = findViewById(R.id.tvDetailMerk);
        tvNoinvD    = findViewById(R.id.tvDetailNoinv);
        tvMapsD     = findViewById(R.id.tvDetailMaps);
        tvlvldetail = findViewById(R.id.tvlvldetail);


        etKetD      = findViewById(R.id.etDetailKeterangan);
        etStatusD   = findViewById(R.id.etDetailStatus);
        etNoruangD  = findViewById(R.id.etDetailNoruang);
        etLantaiD   = findViewById(R.id.etDetailLantai);
        etGedungD   = findViewById(R.id.etDetailGedung);
        etThdipaD   = findViewById(R.id.etDetailThdipa);

        ivListD     = findViewById(R.id.ivDetailList);
        btnDelete   = findViewById(R.id.btnDelete);
        btnUpdate   = findViewById(R.id.btnUpdate);
    }

    private void receiveAndShowData(){
        //RECEIVE DATA FROM ITEMS ACTIVITY VIA INTENT
        Intent i          = this.getIntent();
        String idPer      = i.getExtras().getString("ID_KEY");
        String jenis      = i.getExtras().getString("JENIS_KEY");
        String status     = i.getExtras().getString("STATUS_KEY");
        String noruang    = i.getExtras().getString("NORUANG_KEY");
        String lantai     = i.getExtras().getString("LANTAI_KEY");
        String gedung     = i.getExtras().getString("GEDUNG_KEY");
        String kawasan    = i.getExtras().getString("KAWASAN_KEY");
        String maps       = i.getExtras().getString("MAPS_KEY");
        String merk       = i.getExtras().getString("MERK_KEY");
        String keterangan = i.getExtras().getString("KET_KEY");
        String noinv      = i.getExtras().getString("NOINV_KEY");
        String thdipa     = i.getExtras().getString("THDIPA_KEY");
        String imageURL   = i.getExtras().getString("IMAGE_KEY");

        //SET RECEIVED DATA TO TEXTVIEWS AND IMAGEVIEWS
        tvDetailId.setText(idPer);
        tvJenisD.setText(jenis);
        tvKawasanD.setText(kawasan);
        tvMerkD.setText(merk);
        tvNoinvD.setText(noinv);
        tvMapsD.setText(maps);
        Intent intent = getIntent();
        String lvldetail = intent.getStringExtra(Home.EXTRA_LVL_STOK);
        tvlvldetail.setText(lvldetail);
        String level = tvlvldetail.getText().toString();
        if(level.equals("usr")){
            btnDelete.setVisibility(View.GONE);
        }

        etStatusD.setText(status);
        etNoruangD.setText(noruang);
        etLantaiD.setText(lantai);
        etGedungD.setText(gedung);
        etKetD.setText(keterangan);
        etThdipaD.setText(thdipa);
        Picasso.get().load(imageURL).placeholder(R.mipmap.ic_launcher).into(ivListD);
    }

    private void delete() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(tvDetailId.getText().toString());
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                API api = retrofit.create(API.class);
                Call<Value> call = api.hapus(a);
                call.enqueue(new Callback<Value>() {
                    @Override
                    public void onResponse(Call<Value> call, Response<Value> response) {
                        String value    = response.body().getValue();
                        String message  = response.body().getMessage();
                        if (value.equals("1")) {
                            Intent i = new Intent(DetailStok.this, Perangkat.class);
                            startActivity(i);
                            Toast.makeText(DetailStok.this, message, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(DetailStok.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Value> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(DetailStok.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void ubah() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer tvId        = Integer.valueOf(tvDetailId.getText().toString());
                String etStatus     = etStatusD.getText().toString();
                String etNoruang    = etNoruangD.getText().toString();
                String etLantai     = etLantaiD.getText().toString();
                String etGedung     = etGedungD.getText().toString();
                String etKet        = etKetD.getText().toString();
                String etThdipa     = etThdipaD.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                API api = retrofit.create(API.class);
                Call<Value> call = api.ubah(tvId, etStatus, etNoruang, etLantai, etGedung, etKet, etThdipa);
                call.enqueue(new Callback<Value>() {
                    @Override
                    public void onResponse(Call<Value> call, Response<Value> response) {
                        String value    = response.body().getValue();
                        String message  = response.body().getMessage();
                        if (value.equals("1")) {
                            Intent i = new Intent(DetailStok.this, Perangkat.class);
                            startActivity(i);
                            Toast.makeText(DetailStok.this, message, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(DetailStok.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Value> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(DetailStok.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
