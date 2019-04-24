package com.rizki.inventarisperangkat.AddPerangkat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.rizki.inventarisperangkat.BuildConfig;
import com.rizki.inventarisperangkat.Home;
import com.rizki.inventarisperangkat.Perangkat;
import com.rizki.inventarisperangkat.R;
import com.rizki.inventarisperangkat.Retrofit.ApiConfig;
import com.rizki.inventarisperangkat.Retrofit.AppConfig;
import com.rizki.inventarisperangkat.Retrofit.ServerResponse;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity implements AddView, View.OnClickListener{
    ImageView imageView;
    private AddPresenter addPresenter;
    EditText etAddJenis, etAddStatus, etAddNo_inv, etAddMerk, etAddTh_dipa, etAddLok_no,  etAddLok_lantai, etAddLok_gedung, etAddLok_kawasan, etAddKeterangan;
    Button btnAdd, btn_pick_image;

    //capture image
    private static final int CAMERA_PIC_REQUEST = 1111;
    private static final String TAG = AddActivity.class.getSimpleName();

    public static final int MEDIA_TYPE_IMAGE = 11;
    private Uri fileUri;
    private String mImageFileLocation = "";
    private String foto = "";
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    ProgressDialog pDialog;
    private String postPath;

    //maps
    private static final int MY_PERMISSION_FINE_LOCATION = 101;
    private static final int PLACE_PICKER_REQUEST =1;
    TextView placeNameText;
    TextView placeAddressText;
    Button getPlaceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initPresenter();
        initView();

        //upload image
        btn_pick_image = (Button) findViewById(R.id.Pick_Image_Add);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            btn_pick_image.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        } else {
            btn_pick_image.setEnabled(true);
        }
        btn_pick_image.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        initDialog();

        //deklarasi spinner jenis
        Spinner spinner_jenis = findViewById(R.id.spinner_jenis);
        ArrayAdapter<CharSequence> adapterr = ArrayAdapter.createFromResource(this, R.array.sp_jenis, android.R.layout.simple_spinner_item);
        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_jenis.setAdapter(adapterr);

        //deklarasi spinner merk
        Spinner spinner_merk = findViewById(R.id.spinner_merk);
        ArrayAdapter<CharSequence> adapterr_merk = ArrayAdapter.createFromResource(this, R.array.sp_merk, android.R.layout.simple_spinner_item);
        adapterr_merk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_merk.setAdapter(adapterr_merk);

        //deklarasi spinner kawasan
        Spinner spinner_kawasan = findViewById(R.id.spinner_kawasan);
        ArrayAdapter<CharSequence> adapterr_kawasan = ArrayAdapter.createFromResource(this, R.array.sp_kawasan, android.R.layout.simple_spinner_item);
        adapterr_kawasan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_kawasan.setAdapter(adapterr_kawasan);

        //deklarasi spinner status
        Spinner spinner_status = findViewById(R.id.spinner_status);
        ArrayAdapter<CharSequence> adapterr_status = ArrayAdapter.createFromResource(this, R.array.sp_status, android.R.layout.simple_spinner_item);
        adapterr_status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_status.setAdapter(adapterr_status);

        //maps
        requestPermissions();
        placeNameText = findViewById(R.id.tvLokasiMaps);
        placeAddressText = findViewById(R.id.tvPlaceAddress);
        getPlaceButton = findViewById(R.id.btnLokasi);
        getPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(AddActivity.this);
                    AddActivity.this.startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initView() {
        etAddJenis = findViewById(R.id.etAddJenis);
        etAddNo_inv = findViewById(R.id.etAddNo_inv);
        etAddMerk = findViewById(R.id.etAddMerk);
        etAddTh_dipa = findViewById(R.id.etAddTh_dipa);
        etAddLok_no = findViewById(R.id.etAddLok_no_ruang);
        etAddLok_lantai = findViewById(R.id.etAddLok_lantai);
        etAddLok_gedung = findViewById(R.id.etAddLok_gedung);
        etAddLok_kawasan = findViewById(R.id.etAddLok_kawasan);
        etAddStatus = findViewById(R.id.etAddStatus);
        etAddKeterangan = findViewById(R.id.etAddKeterangan);
        imageView = findViewById(R.id.previewAdd);
        btnAdd = findViewById(R.id.btnAdd);
    }

    private void initPresenter() {
        addPresenter = new AddPresenter(this);
    }

    private void requestPermissions() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                btn_pick_image.setEnabled(true);
            }
        }

        switch (requestCode){
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(), "This app requires location permission to be granted", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(AddActivity.this, data) ;
                placeNameText.setText(place.getName());
                placeAddressText.setText(place.getAddress());
            }
        }
        else if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == RESULT_OK){
                if (Build.VERSION.SDK_INT > 21) {
                    Glide.with(this).load(mImageFileLocation).into(imageView);
                    postPath = mImageFileLocation;
                }else{
                    Glide.with(this).load(fileUri).into(imageView);
                    postPath = fileUri.getPath();
                }
            }
        }
        else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, "Sorry, there was an error!", Toast.LENGTH_LONG).show();
        }
    }

    //tambahan untuk upload image
    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.Pick_Image_Add:
                new MaterialDialog.Builder(this)
                        .title(R.string.uploadImages)
                        .items(R.array.uploadImages)
                        .itemsIds(R.array.itemIds)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {
                                    case 0:
                                        AddActivity.this.captureImage();
                                        break;
                                    case 1:
                                        imageView.setImageResource(R.drawable.ic_launcher_background);
                                        break;
                                }
                            }
                        })
                        .show();
                break;
            case R.id.btnAdd:
                addPresenter.addPerangkat();
                uploadFile();
                break;
        }
    }

    private void captureImage() {
        if (Build.VERSION.SDK_INT > 21) {
            Intent callCameraApplicationIntent = new Intent();
            callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

            //give some instruction to the intent to save the image
            File photoFile = null;

            try {
                // If the createImageFile will be successful, the photo file will have the address of the file
                photoFile = createImageFile();
                // Here we call the function that will try to catch the exception made by the throw function
            } catch (IOException e) {
                Logger.getAnonymousLogger().info("Exception error in generating the file");
                e.printStackTrace();
            }
            // Here we add an extra file to the intent to put the address on to. For this purpose we use the FileProvider, declared in the AndroidManifest.
            Uri outputUri = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile);
            callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

            // The following is a new line with a trying attempt
            callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Logger.getAnonymousLogger().info("Calling the camera App by intent");

            // The following strings calls the camera app and wait for his file in return.
            startActivityForResult(callCameraApplicationIntent, CAMERA_PIC_REQUEST);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_PIC_REQUEST);
        }
    }

    File createImageFile() throws IOException {
        Logger.getAnonymousLogger().info("Generating the image - method started");

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmSS").format(new Date());
        String imageFileName = "PERANGKAT_" + timeStamp;
        // Here we specify the environment location and the exact path where we want to save the so-created file
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/photo_saving_app");
        Logger.getAnonymousLogger().info("Storage directory set");

        // Then we create the storage directory if does not exists
        if (!storageDirectory.exists()) storageDirectory.mkdir();

        // Here we create the file using a prefix, a suffix and a directory
        File image = new File(storageDirectory, imageFileName + ".jpg");

        // Here the location is saved into the string mImageFileLocation
        Logger.getAnonymousLogger().info("File name and path set");

        mImageFileLocation = image.getAbsolutePath();
        foto = imageFileName+".jpg";
        return image;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "PERANGKAT_" + ".jpg");
        }  else {
            return null;
        }

        return mediaFile;
    }

    private void uploadFile() {
        if (postPath == null || postPath.equals("")) {
            Toast.makeText(this, "please select an image ", Toast.LENGTH_LONG).show();
            return;
        } else {
            showpDialog();

            // Map is used to multipart the file using okhttp3.RequestBody
            Map<String, RequestBody> map = new HashMap<>();
            File file = new File(postPath);

            // Parsing any Media type file
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            map.put("file\"; filename=\"" + file.getName() + "\"", requestBody);
            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Call<ServerResponse> call = getResponse.upload("token", map);
            call.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    if (response.isSuccessful()){
                        if (response.body() != null){
                            hidepDialog();
                            ServerResponse serverResponse = response.body();
                            Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }else {
                        hidepDialog();
                        Toast.makeText(getApplicationContext(), "problem uploading image", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    hidepDialog();
                    Log.v("Response gotten is", t.getMessage());
                }
            });
        }
    }

    private void initDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.msg_loading));
        pDialog.setCancelable(true);
    }

    protected void showpDialog() {
        if (!pDialog.isShowing()) pDialog.show();
    }

    protected void hidepDialog() {
        if (pDialog.isShowing()) pDialog.dismiss();
    }

    @Override
    public String getJenis() {
        return etAddJenis.getText().toString();
    }

    @Override
    public String getNo_inv() {
        return etAddNo_inv.getText().toString();
    }

    @Override
    public String getMerk() {
        return etAddMerk.getText().toString();
    }

    @Override
    public String getTh_Dipa() {
        return etAddTh_dipa.getText().toString();
    }

    @Override
    public String getLok_no_ruang() {
        return etAddLok_no.getText().toString();
    }

    @Override
    public String getLok_lantai() {
        return etAddLok_lantai.getText().toString();
    }

    @Override
    public String getLok_gedung() {
        return etAddLok_gedung.getText().toString();
    }

    @Override
    public String getLok_kawasan() {
        return etAddLok_kawasan.getText().toString();
    }

    @Override
    public String getStatus() {
        return etAddStatus.getText().toString();
    }

    @Override
    public String getKeterangan() {
        return etAddKeterangan.getText().toString();
    }

    @Override
    public String getFoto() {
        return foto;
    }

    @Override
    public String getLokasi_maps() {
        return placeAddressText.getText().toString();
    }

    @Override
    public void successAddPerangkat() {
        Toast.makeText(this, "Berhasil Menambah Perangkat", Toast.LENGTH_SHORT).show();
        Intent home = new Intent(AddActivity.this, Perangkat.class);
        startActivity(home);
        finish();
    }

    @Override
    public void failedAddPerangkat() {
        Toast.makeText(this, "Maaf, Gagal Menambah Perangkat", Toast.LENGTH_SHORT).show();
    }
}