package com.jay.reco;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout rellay1, rellay2;
    Button login;
    String username = "Username";
    String password = "123456";
    EditText et_email,et_password;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);

        rellay1 = findViewById(R.id.rellay1);
        rellay2 = findViewById(R.id.rellay2);
        login = findViewById(R.id.btn_login);
        et_email = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String uname=et_email.getText().toString().trim();
        String pass=et_password.getText().toString().trim();
        if (uname.equalsIgnoreCase(username)) {
            if (pass.equalsIgnoreCase(password)) {
                Toast.makeText(SplashScreen.this, "Login successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
            }
        }
        else {
            Toast.makeText(SplashScreen.this, "Invalid username or password...!", Toast.LENGTH_SHORT).show();
        }
    }

}


