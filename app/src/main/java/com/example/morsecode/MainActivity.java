package com.example.morsecode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button e2m,m2e,help,translate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        e2m=findViewById(R.id.etm);
        m2e=findViewById(R.id.mte);
        help=findViewById(R.id.help);
        translate=findViewById(R.id.translate);

        help.setOnClickListener(v -> {
            Intent intent =new Intent(MainActivity.this,HelpWindow.class);
            startActivity(intent);
        });

        e2m.setOnClickListener(v -> {
            Intent intent =new Intent(MainActivity.this,LengthSelect.class);
            intent.putExtra("key",2);
            startActivity(intent);
        });

        m2e.setOnClickListener(v -> {
            Intent intent =new Intent(MainActivity.this,LengthSelect.class);
            intent.putExtra("key",1);
            startActivity(intent);
        });

        translate.setOnClickListener(v -> {
            Intent intent =new Intent(MainActivity.this,Translate.class);
            startActivity(intent);
        });
    }
}