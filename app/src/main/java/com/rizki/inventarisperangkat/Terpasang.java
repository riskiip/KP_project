package com.rizki.inventarisperangkat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;
import com.rizki.inventarisperangkat.AddPerangkat.AddActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class Terpasang extends AppCompatActivity {
    public static final String EXTRA_LVL_PASANG = "com.rizki.inventarisperangkat.EXTRA_LVL_PASANG";
    FloatingActionButton fabTerpasang;
    private static final String BASE_URL = "http://layanan.batan.go.id/mobilepjkkd/";
    private Terpasang.ListViewAdapter adapter;
    private ListView mListView;
    TextView tvlvlHome;

    private void populateListView(List<Terpasang.ModelData> perangkatLists) {
        mListView = findViewById(R.id.mListView);
        adapter = new Terpasang.ListViewAdapter(this,perangkatLists);
        mListView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fabTerpasang = findViewById(R.id.fabHome);
        fabTerpasang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tambah = new Intent(Terpasang.this, AddActivity.class);
                startActivity(tambah);
                finish();
            }
        });

        tvlvlHome = findViewById(R.id.tvlvlHome);
        Intent intent = getIntent();
        String lvlHome = intent.getStringExtra(Perangkat.EXTRA_LVL4);
        tvlvlHome.setText(lvlHome);

        final ProgressBar myProgressBar= findViewById(R.id.myProgressBar);
        myProgressBar.setIndeterminate(true);
        myProgressBar.setVisibility(View.VISIBLE);

        /*Create handle for the RetrofitInstance interface*/
        Terpasang.MyAPIService myAPIService = Terpasang.RetrofitClientInstance.getRetrofitInstance().create(Terpasang.MyAPIService.class);
        Call<List<Terpasang.ModelData>> call = myAPIService.getData();
        call.enqueue(new Callback<List<Terpasang.ModelData>>() {

            @Override
            public void onResponse(Call<List<Terpasang.ModelData>> call, Response<List<Terpasang.ModelData>> response) {
                myProgressBar.setVisibility(View.GONE);
                populateListView(response.body());
            }
            @Override
            public void onFailure(Call<List<Terpasang.ModelData>> call, Throwable throwable) {
                myProgressBar.setVisibility(View.GONE);
                Toast.makeText(Terpasang.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    class ModelData {
        @SerializedName("id")
        private Integer id;
        @SerializedName("jenis")
        private String jenis;
        @SerializedName("no_inv")
        private String no_inv;
        @SerializedName("merk")
        private String merk;
        @SerializedName("th_dipa")
        private String th_dipa;
        @SerializedName("lok_no_ruang")
        private String lok_no_ruang;
        @SerializedName("lok_lantai")
        private String lok_lantai;
        @SerializedName("lok_gedung")
        private String lok_gedung;
        @SerializedName("lok_kawasan")
        private String lok_kawasan;
        @SerializedName("status")
        private String status;
        @SerializedName("keterangan")
        private String keterangan;
        @SerializedName("foto")
        private String foto;
        @SerializedName("lokasi_maps")
        private String lokasi_maps;

        public ModelData(int id, String jenis, String no_inv, String merk, String th_dipa, String lok_no_ruang, String lok_lantai, String lok_gedung, String lok_kawasan, String status, String keterangan, String foto, String lokasi_maps) {
            this.id = id;
            this.jenis = jenis;
            this.no_inv = no_inv;
            this.merk = merk;
            this.th_dipa = th_dipa;
            this.lok_no_ruang = lok_no_ruang;
            this.lok_lantai = lok_lantai;
            this.lok_gedung = lok_gedung;
            this.lok_kawasan = lok_kawasan;
            this.status = status;
            this.keterangan = keterangan;
            this.foto = foto;
            this.lokasi_maps = lokasi_maps;
        }

        //Membuat Setter & Getter
        public Integer getId() {
            return id;
        }
        public void setId(Integer id) {
            this.id = id;
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

    interface MyAPIService {
        @GET("read_data_pasang.php")
        Call<List<Terpasang.ModelData>> getData();
    }

    static class RetrofitClientInstance {
        private static Retrofit retrofit;
        public static Retrofit getRetrofitInstance() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }

    public class ListViewAdapter extends BaseAdapter implements Filterable {

        public List<Terpasang.ModelData> dataPerangkat, dataFull;
        private Context context;

        public ListViewAdapter(Context context,List<Terpasang.ModelData> dataPerangkat){
            this.context = context;
            this.dataPerangkat = dataPerangkat;
            dataFull = new ArrayList<>(dataPerangkat);
        }

        @Override
        public int getCount() {
            return dataPerangkat.size();
        }

        @Override
        public Object getItem(int pos) {
            return dataPerangkat.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view= LayoutInflater.from(context).inflate(R.layout.model,viewGroup,false);
            }

            TextView jenis = view.findViewById(R.id.tvJenis);
            TextView status = view.findViewById(R.id.tvStatus);
            TextView noruang = view.findViewById(R.id.tvNoRuang2);
            TextView lantai = view.findViewById(R.id.tvLantai2);
            TextView gedung = view.findViewById(R.id.tvGedung2);
            TextView kawasan = view.findViewById(R.id.tvKawasan2);
            ImageView spacecraftImageView = view.findViewById(R.id.ivList);

            final Terpasang.ModelData Perangkats= dataPerangkat.get(position);

            jenis.setText(Perangkats.getJenis());
            status.setText(Perangkats.getStatus());
            noruang.setText(Perangkats.getLok_no_ruang());
            lantai.setText(Perangkats.getLok_lantai());
            gedung.setText(Perangkats.getLok_gedung());
            kawasan.setText(Perangkats.getLok_kawasan());

            if(Perangkats.getFoto() != null && Perangkats.getFoto().length()>0)
            {
                Picasso.get().load(BASE_URL+"/images/"+Perangkats.getFoto()).placeholder(R.mipmap.ic_launcher).into(spacecraftImageView);
            }else {
                Picasso.get().load(R.mipmap.ic_launcher).into(spacecraftImageView);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, Perangkats.getJenis(), Toast.LENGTH_SHORT).show();
                    String[] perangkats = {
                            String.valueOf(Perangkats.getId()),
                            Perangkats.getJenis(),
                            Perangkats.getStatus(),
                            Perangkats.getLok_no_ruang(),
                            Perangkats.getLok_lantai(),
                            Perangkats.getLok_gedung(),
                            Perangkats.getLok_kawasan(),
                            Perangkats.getLokasi_maps(),
                            Perangkats.getMerk(),
                            Perangkats.getKeterangan(),
                            Perangkats.getNo_inv(),
                            Perangkats.getTh_dipa(),
                            BASE_URL+"/images/"+Perangkats.getFoto()
                    };
                    openDetailActivity(perangkats);
                }
            });
            return view;
        }
        private void openDetailActivity(String[] data) {
            String lvlPasang = tvlvlHome.getText().toString();
            Intent intent = new Intent(Terpasang.this, DetailPasang.class);
            intent.putExtra("ID_KEY", data[0]);
            intent.putExtra("JENIS_KEY", data[1]);
            intent.putExtra("STATUS_KEY", data[2]);
            intent.putExtra("NORUANG_KEY", data[3]);
            intent.putExtra("LANTAI_KEY", data[4]);
            intent.putExtra("GEDUNG_KEY", data[5]);
            intent.putExtra("KAWASAN_KEY", data[6]);
            intent.putExtra("MAPS_KEY", data[7]);
            intent.putExtra("MERK_KEY", data[8]);
            intent.putExtra("KET_KEY", data[9]);
            intent.putExtra("NOINV_KEY", data[10]);
            intent.putExtra("THDIPA_KEY", data[11]);
            intent.putExtra("IMAGE_KEY", data[12]);
            intent.putExtra(EXTRA_LVL_PASANG, lvlPasang);
            startActivity(intent);
        }

        @Override
        public Filter getFilter() {
            return perangkatFilter;
        }

        private Filter perangkatFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Terpasang.ModelData> filteredList = new ArrayList<>();

                if(constraint == null || constraint.length() == 0){
                    filteredList.addAll(dataFull);
                }
                else{
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for(Terpasang.ModelData item : dataFull){
                        if(item.getJenis().toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataPerangkat.clear();
                dataPerangkat.addAll((Collection<? extends Terpasang.ModelData>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}