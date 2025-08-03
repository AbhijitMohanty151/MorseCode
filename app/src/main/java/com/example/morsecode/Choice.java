package com.example.morsecode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Choice extends AppCompatActivity {

    private Button m2e,e2m,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choice);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        m2e=findViewById(R.id.m2e);
        e2m=findViewById(R.id.e2m);
        back=findViewById(R.id.back3);
        back.setOnClickListener(v -> {
            finish();
        });
        e2m.setOnClickListener(v -> {
            Intent intent =new Intent(Choice.this,LengthSelect.class);
            intent.putExtra("key",2);
            startActivity(intent);
        });

        m2e.setOnClickListener(v -> {
            Intent intent =new Intent(Choice.this,LengthSelect.class);
            intent.putExtra("key",1);
            startActivity(intent);
        });
    }
}