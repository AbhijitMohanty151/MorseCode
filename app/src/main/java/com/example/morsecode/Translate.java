package com.example.morsecode;

import android.Manifest;
import androidx.annotation.Nullable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.HashMap;

public class Translate extends AppCompatActivity {

    private EditText input;
    private TextView eng, morse;
    private Button translate;
    private ImageButton cam;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    private Uri photoUri;
    private HashMap<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        input = findViewById(R.id.inputtext);
        eng = findViewById(R.id.eng);
        morse = findViewById(R.id.morse);
        translate = findViewById(R.id.tr);
        cam = findViewById(R.id.cam);

        initMap();

        cam.setOnClickListener(v -> showImageSourceDialog());

        translate.setOnClickListener(v -> convertToMorseOrEnglish());
    }

    private void showImageSourceDialog() {
        String[] options = {"Camera", "Gallery"};
        new AlertDialog.Builder(this)
                .setTitle("Choose image source")
                .setItems(options, (dialog, which) -> {
                    if(which == 0){
                        //camera
                        openCamera();
                    } else {
                        //gallery
                        openGallery();
                    }
                }).show();
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void openGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    101);
        }
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Use photoUri from camera capture
            recognizeTextFromImage(photoUri);

        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            // Get selected image from gallery
            Uri selectedImage = data.getData();
            recognizeTextFromImage(selectedImage);
        }
    }

    private void recognizeTextFromImage(Uri uri) {
        try {
            Bitmap bitmap;
            if (Build.VERSION.SDK_INT >= 29) {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);
                bitmap = ImageDecoder.decodeBitmap(source);
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }

            InputImage image = InputImage.fromBitmap(bitmap, 0);
            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

            recognizer.process(image)
                    .addOnSuccessListener(result -> {
                        StringBuilder extractedText = new StringBuilder();
                        for (Text.TextBlock block : result.getTextBlocks()) {
                            extractedText.append(block.getText()).append("\n");
                        }
                        input.setText(extractedText.toString().trim());
                    })
                    .addOnFailureListener(e -> input.setText("Failed: " + e.getMessage()));

        } catch (IOException e) {
            input.setText("Image decode failed");
            e.printStackTrace();
        }
    }

    private void convertToMorseOrEnglish() {
        String s = input.getText().toString().trim() + " ";
        StringBuilder eng_res = new StringBuilder();
        StringBuilder morse_res = new StringBuilder();

        if (s.isEmpty()) return;

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '.' || ch == '-') {
                int x = giveNext(s,i+1);
                if (x == -1) break;
                String code = s.substring(i, x);
                eng_res.append(map.getOrDefault(code, " "));
                morse_res.append(" ").append(code);
                i = x-1;
            } else if (ch == ' ') {
                eng_res.append(" ");
                morse_res.append(" ");
            } else {
                String lowerChar = String.valueOf(ch).toLowerCase();
                morse_res.append(" ").append(map.getOrDefault(lowerChar, " "));
                eng_res.append(ch);
            }
        }

        eng.setText(eng_res.toString());
        morse.setText(morse_res.toString());
    }

    private int giveNext(String s, int i){
        int l=s.length();
        for(;i<l && (s.charAt(i)=='.' || s.charAt(i)=='.') ;i++);
        return (i==l)?-1:i;
    }
    private void initMap() {

        String[] characters = {
                "a", "b", "c", "d", "e", "f", "g",
                "h", "i", "j", "k", "l", "m", "n",
                "o", "p", "q", "r", "s", "t", "u",
                "v", "w", "x", "y", "z",
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                ",", "?", "'", "!", "/", "(", ")", "&", ":", ";", "=", "+", "_", "\"", "@", " ", "\n"
        };

        String[] morseCodes = {
                ".-", "-...", "-.-.", "-..", ".", "..-.", "--.",
                "....", "..", ".---", "-.-", ".-..", "--", "-.",
                "---", ".--.", "--.-", ".-.", "...", "-", "..-",
                "...-", ".--", "-..-", "-.--", "--..",
                "-----", ".----", "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.",
                "--..--", "..--..", ".----.", "-.-.--", "-..-.", "-.--.", "-.--.-", ".-...", "---...", "-.-.-.", "-...-", ".-.-.", "..--.-", ".-..-.", ".--.-.", "/", "\n"
        };

        for (int i = 0; i < characters.length; i++) {
            String ch = characters[i];
            String code = morseCodes[i];

            // Normalize keys to lowercase for consistency
            map.put(ch.toLowerCase(), code);
            map.put(code, ch); // Morse-to-char (return as is, not uppercased)
        }
    }

}
