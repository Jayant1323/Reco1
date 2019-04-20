package com.jay.reco;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        String savedExtra = getIntent().getStringExtra("Text");
        EditText myText = findViewById(R.id.textID);
        myText.setText(savedExtra);
    }
}
