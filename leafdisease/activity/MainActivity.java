package com.samcore.leafdisease.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.samcore.leafdisease.R;
import com.samcore.leafdisease.components.Classifier;
import com.samcore.leafdisease.components.PopupAlertWithTTS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {
    private Classifier mClassifier;
    private Bitmap mBitmap;

    private Dialog myDialog;

    private String pname="";
    private String pSymptoms="";
    private String pManage="";

    private TextView NameV;
    private TextView SymptomsV;
    private TextView ManageV;
    private ImageView selectImage;

    private final int mCameraRequestCode = 0;
    private final int mInputSize = 200; //224
    private final String mModelPath = "soil.tflite";
    private final String mLabelPath = "labels.txt";
    private static final int FILE_CHOOSER_REQUEST_CODE = 123;

    PopupAlertWithTTS popupAlertWithTTS;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);


        mClassifier = new Classifier(getAssets(), mModelPath, mLabelPath, mInputSize);

        myDialog= new Dialog(this);
        popupAlertWithTTS=new PopupAlertWithTTS(getApplicationContext());

        findViewById(R.id.disease_info).setOnClickListener(view -> customDialog());

        selectImage=findViewById(R.id.select_image1);

        selectImage.setOnClickListener(v -> {
            showFileChooser();
        });

        findViewById(R.id.mCameraButton).setOnClickListener(view -> {
            Intent callCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(callCameraIntent, mCameraRequestCode);
        });


        Intent callCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(callCameraIntent, mCameraRequestCode);
    }
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FILE_CHOOSER_REQUEST_CODE);
    }

    private void customDialog() {
        myDialog.setContentView(R.layout.detail_dailog_act);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        myDialog.show();

        NameV = myDialog.findViewById(R.id.pltd_name);
        SymptomsV = myDialog.findViewById(R.id.symptoms);
        ManageV = myDialog.findViewById(R.id.management);

        NameV.setText(((TextView)findViewById(R.id.mResultTextView)).getText());

        String Sname = NameV.getText().toString();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray jArray = obj.getJSONArray("plant_disease");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject plant = jArray.getJSONObject(i);
                pname = plant.getString("name");

                if (Sname.equals(pname)) {
                    pSymptoms = plant.getString("symptoms");
                    pManage = plant.getString("management");
                }
                SymptomsV.setText(pSymptoms);
                ManageV.setText(pManage);
                Log.d(SymptomsV.toString(), ManageV.toString());
            }
            String message = "Disease name: " + Sname + "\nDisease symptoms: " + pSymptoms + "\nDisease management: " + pManage;
            popupAlertWithTTS.showAlertWithTTS("Disease Information", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream inputStream = this.getAssets().open("data.json");
            int size=inputStream.available();
            byte[] buffer=new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json=new String(buffer, Charset.forName("UTF-8"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return json;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == mCameraRequestCode){
            if(resultCode == Activity.RESULT_OK && data != null) {
                mBitmap = (Bitmap) data.getExtras().get("data");
                mBitmap = scaleImage(mBitmap);
                ((ImageView)findViewById(R.id.mPhotoImageView)).setImageBitmap(mBitmap);
                Classifier.Recognition model_output = mClassifier.recognizeImage(scaleImage(mBitmap)).get(0);
                ((TextView)findViewById(R.id.mResultTextView)).setText(model_output.title);
                //mResultTextView.text = model_output?.title + "\n" + model_output?.confidence
                // ADD CONFIDENCE TO ANOTHER TEXTVIEW FOR EASIER CODING
                ((TextView)findViewById(R.id.mResultTextView_2)).setText(Float.toString(model_output.confidence));
            }
        }
        if (requestCode == FILE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap != null) {
                        // Scale the bitmap
                        bitmap = scaleImage(bitmap);

                        // Display the bitmap in ImageView
                        ((ImageView) findViewById(R.id.mPhotoImageView)).setImageBitmap(bitmap);

                        // Run classification on the scaled bitmap
                        Classifier.Recognition modelOutput = mClassifier.recognizeImage(bitmap).get(0);

                        // Update UI with classification result
                        ((TextView) findViewById(R.id.mResultTextView)).setText(modelOutput.title);
                        ((TextView) findViewById(R.id.mResultTextView_2)).setText(Float.toString(modelOutput.confidence));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public Bitmap scaleImage(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaledWidth = mInputSize / (float)width;
        float scaledHeight = mInputSize / (float)height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaledWidth, scaledHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}
