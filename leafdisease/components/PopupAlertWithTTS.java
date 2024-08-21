package com.samcore.leafdisease.components;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

public class PopupAlertWithTTS {
    private TextToSpeech textToSpeech;
    private Context context;

    public PopupAlertWithTTS(Context context) {
        this.context = context;
        initializeTextToSpeech();
    }

    private void initializeTextToSpeech() {
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.getDefault());
                    // Set speech rate (0.5 is slower than normal)
                    textToSpeech.setSpeechRate(1.0f); // Adjust this value as needed
                } else {
                    Toast.makeText(context, "Error initializing Text-to-Speech", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showAlertWithTTS(String title, String message) {
        // Create the AlertDialog.Builder
      /*  AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        dialog.dismiss();
                    }
                });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();*/

        // Speak the message using Text-to-Speech
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    // Release resources associated with TextToSpeech
    public void release() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
