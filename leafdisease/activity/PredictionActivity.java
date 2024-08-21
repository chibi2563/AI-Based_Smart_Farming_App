        package com.samcore.leafdisease.activity;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.samcore.leafdisease.databinding.ActivityPredictionBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class PredictionActivity extends AppCompatActivity {
    int k, l,m,n;
    Button check_btn;

     TextView prediction,txtString,txtStringLength, sensorView6, sensorView1, sensorView2, sensorView3, sensorView4,sensorView5;

    EditText n1,p1,k1;
    TextView txt_temp,txt_humidity,txt_gas,txt_light;
    private ActivityPredictionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityPredictionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.checkBtn.setOnClickListener(v -> {
            double n = Double.parseDouble(binding.n1.getText().toString());
            double p = Double.parseDouble(binding.p.getText().toString());
            double k = Double.parseDouble(binding.k.getText().toString());
            double temperature = Double.parseDouble(binding.editTemp.getText().toString());
            double humidity = Double.parseDouble(binding.editHumidity.getText().toString());
            double pH = Double.parseDouble(binding.editPh.getText().toString());
            double rainfall = Double.parseDouble(binding.editRainfall.getText().toString());

            int intN = (int) n;
            int intP = (int) p;
            int intK = (int) k;
            int intTemperature = (int) temperature;
            int intHumidity = (int) humidity;
            int intPH = (int) pH;
            int intRainfall = (int) rainfall;



            getLabelValue(this, intN, intP, intK, intTemperature, intHumidity, intPH, intRainfall);
            /*if ((n2>=0 && 30>=n2) && (p2>=0 && 40>=p2) && (k2>=0 && 40>=k2))
            {
                prediction.setText("Rice");
            }
            else  if ((n2>=31 && 50>=n2) && (p2>=41 && 50>=p2) && (k2>=31 && 50>=k2))
            {
                prediction.setText("Wheat");
            }
            else  if ((n2>=51 && 70>=n2) && (p2>=51 && 60>=p2) && (k2>=51 && 70>=k2))
            {
                prediction.setText("Corn");
            }
            else  if ((n2>=71 && 100>=n2) && (p2>=61 && 80>=p2) && (k2>=71 && 100>=k2))
            {
                prediction.setText("Groundnut");
            }*/
        });

    }

    private static void getLabelValue(PredictionActivity predictionActivity, int N, int P, int K, int temperature, int humidity, int pH, int rainfall) {
        try {
            // Open the CSV file from the assets folder
            InputStream inputStream = predictionActivity.getResources().getAssets().open("crop_recommendation.csv");
            Reader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Create a CSVReader object
            CSVReader csvReader = new CSVReader(reader);

            // Read the header row
            String[] header = csvReader.readNext();

            // Process each row
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                // Assuming the structure of the row is as follows:
                // N, P, K, temperature, humidity, pH, rainfall, label
                int rowN = Integer.parseInt(row[0]);
                int rowP = Integer.parseInt(row[1]);
                int rowK = (int) Double.parseDouble(row[2]);
                int rowTemperature = (int) Double.parseDouble(row[3]);
                int rowHumidity = (int) Double.parseDouble(row[4]);
                int rowpH = (int) Double.parseDouble(row[5]);
                int rowRainfall = (int) Double.parseDouble(row[6]);
                String label = row[7];

                Log.d("rowRainfall", "rowRainfall: " + rowRainfall);

                // Compare the parameters with the values in the current row
                if (N == rowN && P == rowP && K == rowK && temperature == rowTemperature &&
                        humidity == rowHumidity && pH == rowpH && rainfall == rowRainfall) {
                    // If the parameters match, log or use the corresponding label
                    Log.d("Label", "Label: " + label);
                    predictionActivity.binding.prediction.setText(label);
                    // You can return the label here or use it as needed
                    return;
                }
            }

            // Close the CSVReader
            csvReader.close();

        } catch (IOException | CsvValidationException | NumberFormatException e) {
            e.getMessage();
        }
    }
    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause()
    {
       super.onPause();
    }
}
