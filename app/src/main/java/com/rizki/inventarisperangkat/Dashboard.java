package com.rizki.inventarisperangkat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.Dash;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    private CardView cardPerangkat, cardData, cardSensus;
    public static final String EXTRA_LVL2 = "com.rizki.inventarisperangkat.EXTRA_LVL2";
    TextView tvUsr, tvLvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        cardPerangkat = findViewById(R.id.cardPerangkat);
        cardData = findViewById(R.id.cardDataCenter);
        cardSensus = findViewById(R.id.cardSensuspc);

        cardPerangkat.setOnClickListener(this);
        cardData.setOnClickListener(this);
        cardSensus.setOnClickListener(this);

        tvUsr = findViewById(R.id.tvUser);
        tvLvl = findViewById(R.id.tvLvl);

        Intent intent = getIntent();
        String usr = intent.getStringExtra(Login.EXTRA_USER);
        String lvl = intent.getStringExtra(Login.EXTRA_LVL);
        tvUsr.setText(usr);
        tvLvl.setText(lvl);

    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch(v.getId()){
            case R.id.cardPerangkat :
                String lvl2 = tvLvl.getText().toString();
                i = new Intent(Dashboard.this, Perangkat.class);
                i.putExtra(EXTRA_LVL2, lvl2);
                startActivity(i);
                break;
            case R.id.cardDataCenter : i = new Intent(Dashboard.this, DataCenter.class);startActivity(i);
                break;
            case R.id.cardSensuspc : i = new Intent(Dashboard.this, Sensuspc.class);startActivity(i);
            default:break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                new AlertDialog.Builder(this)
                        .setMessage("Apa Anda yakin ingin Logout?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent logout = new Intent(getApplicationContext(), Login.class);
                                startActivity(logout);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
        }
        return  true;
    }
}