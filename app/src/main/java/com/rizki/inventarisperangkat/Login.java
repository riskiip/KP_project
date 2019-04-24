package com.rizki.inventarisperangkat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    public static final String EXTRA_USER = "com.rizki.inventarisperangkat.EXTRA_USER";
    public static final String EXTRA_LVL = "com.rizki.inventarisperangkat.EXTRA_LVL";

    EditText etUsername, etPassword, etLevel;
    public String PasswordHolder, UsernameHolder, LevelHolder;
    String finalResult ;
    public String usr_level;
    String Login_url ="http://layanan.batan.go.id/mobilepjkkd/Login.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    RelativeLayout rellay1;
    Button btn_Login;
    Handler handlder = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rellay1 = findViewById(R.id.rellay1);
        handlder.postDelayed(runnable, 3000);

        etLevel=findViewById(R.id.etLevel);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btn_Login = findViewById(R.id.btn_Login);
        btn_Login.setOnClickListener(view -> {


            CheckEditTextIsEmptyOrNot();

            if (CheckEditText) {

                UserLoginFunction(UsernameHolder, PasswordHolder, LevelHolder);

            } else {

                Toast.makeText(Login.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void CheckEditTextIsEmptyOrNot() {

        LevelHolder = etLevel.getText().toString();
        UsernameHolder = etUsername.getText().toString();
        PasswordHolder = etPassword.getText().toString();


        if (TextUtils.isEmpty(UsernameHolder) || TextUtils.isEmpty(LevelHolder) || TextUtils.isEmpty(PasswordHolder)) {
            CheckEditText = false;
        } else {

            CheckEditText = true;
        }

    }

    public void UserLoginFunction(final String username, final String password, final String level){

        class UserLoginClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(Login.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if(httpResponseMsg.equalsIgnoreCase("Data Matched")){

                    Intent done = new Intent(getApplicationContext(), Dashboard.class);
                    done.putExtra(EXTRA_USER, UsernameHolder);
                    done.putExtra(EXTRA_LVL, LevelHolder);
                    startActivity(done);
                    finish();
                }
//
//                else if(httpResponseMsg.equalsIgnoreCase("anda bukan admin sistem")) {
//
//                    Intent done = new Intent(getApplicationContext(), MainActivity2.class);
//                    startActivity(done);
//                    finish();
//
//                }
                else{
                    Toast.makeText(Login.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("username",params[0]);
                hashMap.put("password",params[1]);
                hashMap.put("level", params[2]);

                finalResult = httpParse.postRequest(hashMap, Login_url);
                return finalResult;
            }
        }

        UserLoginClass LoginClass = new UserLoginClass();

        LoginClass.execute(username,password,level);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please Click Back Again to Exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }
}
