package com.rizki.inventarisperangkat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

public class Perangkat extends AppCompatActivity implements View.OnClickListener {
    private CardView cardStok, cardPasang, cardRusak;
    public static final String EXTRA_LVL3 = "com.rizki.inventarisperangkat.EXTRA_LVL3";
    public static final String EXTRA_LVL4 = "com.rizki.inventarisperangkat.EXTRA_LVL4";
    public static final String EXTRA_LVL5 = "com.rizki.inventarisperangkat.EXTRA_LVL5";
    TextView tvlvlPer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perangkat);
        cardStok = findViewById(R.id.cardStok);
        cardPasang = findViewById(R.id.cardPasang);
        cardRusak = findViewById(R.id.cardRusak);

        cardStok.setOnClickListener(this);
        cardPasang.setOnClickListener(this);
        cardRusak.setOnClickListener(this);

        tvlvlPer = findViewById(R.id.tvlvlPer);
        Intent intent = getIntent();
        String getlvl = intent.getStringExtra(Dashboard.EXTRA_LVL2);
        tvlvlPer.setText(getlvl);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch(v.getId()){
            case R.id.cardStok :
                String lvl3 = tvlvlPer.getText().toString();
                i = new Intent(this, Home.class);
                i.putExtra(EXTRA_LVL3, lvl3);
                startActivity(i);
                break;
            case R.id.cardPasang :
                String lvl4 = tvlvlPer.getText().toString();
                i = new Intent(this, Terpasang.class);
                i.putExtra(EXTRA_LVL4, lvl4);
                startActivity(i);
                break;
            case R.id.cardRusak :
                String lvl5 = tvlvlPer.getText().toString();
                i = new Intent(this, Rusak.class);
                i.putExtra(EXTRA_LVL5, lvl5);
                startActivity(i);
                break;
            default:break;
        }

    }
}
