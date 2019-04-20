package com.jay.reco;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.Objects;

import androidx.annotation.RequiresApi;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class RecognitionFragment extends Fragment {

    public RecognitionFragment() {
    }

    private final int TEXT_RECO_REQ_CODE = 20000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_recognition, container, false);
        Button snapBtn = view.findViewById(R.id.snapBtn);
        snapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextReco(view);
            }
        });
        return view;
    }
    public void TextReco(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,TEXT_RECO_REQ_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==TEXT_RECO_REQ_CODE){
            if(resultCode == RESULT_OK){
                Bitmap Photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                TextRecognition(Photo);
            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getContext(),"Operation Cancelled by User",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getContext(),"Failed to Capture Image",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void TextRecognition(Bitmap Photo) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(Photo);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();
        detector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                for (FirebaseVisionText.TextBlock block: firebaseVisionText.getTextBlocks()) {
                    String text = block.getText();
                    Intent myIntent = new Intent(getActivity(),ActivityDisplay.class);
                    myIntent.putExtra("Text", text);
                    startActivity(myIntent);

                }
            }
        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"Failed to Recognise Text From Image", Toast.LENGTH_LONG).show();
                            }
                        });
    }
}
