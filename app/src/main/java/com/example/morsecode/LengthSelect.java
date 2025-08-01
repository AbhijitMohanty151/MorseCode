package com.example.morsecode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LengthSelect extends AppCompatActivity {

    private Button next;
    private TextView err;
    private EditText min,max;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_length_select);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        next=findViewById(R.id.next);
        err=findViewById(R.id.err);
        min=findViewById(R.id.min);
        max=findViewById(R.id.max);
        int key=getIntent().getIntExtra("key",0);

        next.setOnClickListener(v -> {
            String mi=min.getText().toString();
            String ma=max.getText().toString();
            if(mi.isEmpty()||ma.isEmpty()){
                err.setText("*Please Enter Values");
                return;
            }
            int m1=Integer.parseInt(mi);
            int m2=Integer.parseInt(ma);
            if(m1>m2){
                err.setText("*Minimum Length cannot be greater than Maximum Length");
                return;
            }
            if(m1<=0){
                err.setText("*Minimum Length cannot be less than or eqyual to 0");
                return;
            }
            if(m2<=0){
                err.setText("*Maximum Length cannot be less than or eqyual to 0");
                return;
            }
            if(m1>26){
                err.setText("*Minimum Length cannot be greater than 26");
                return;
            }
            err.setText("");
            Intent i=new Intent(LengthSelect.this,test.class);
            i.putExtra("min",m1);
            i.putExtra("max",m2);
            i.putExtra("key",key);
            startActivity(i);
        });
    }
}