package com.jay.reco;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.Objects;

import androidx.annotation.RequiresApi;

public class Recognition extends AppCompatActivity {

    private final int TEXT_RECO_REQ_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition);

        Button snapBtn = findViewById(R.id.snapBtn);
        snapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextReco(view);
            }
        });
    }

    public void TextReco(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,TEXT_RECO_REQ_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==TEXT_RECO_REQ_CODE){
            if(resultCode == RESULT_OK){
                Bitmap Photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                TextRecognisation(Photo);
            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this,"Operation Cancelled by User",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this,"Failed to Capture Image",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void TextRecognisation(Bitmap Photo) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(Photo);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();
        Task<FirebaseVisionText> Result;
        Result = detector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                for (FirebaseVisionText.TextBlock block: firebaseVisionText.getTextBlocks()) {
                    String text = block.getText();
                    Point[] cornerPoints = block.getCornerPoints();
                    Rect boundingBox = block.getBoundingBox();
                    //Toast.makeText(MainActivity.this,"Element: "+element.getText(),Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(getBaseContext(),ActivityDisplay.class);
                    myIntent.putExtra("Text", text);
                    startActivity(myIntent);

                }
            }
        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Recognition.this,"Failed to Recognise Text From Image", Toast.LENGTH_LONG).show();
                            }
                        });
    }
}
