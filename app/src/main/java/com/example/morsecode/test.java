package com.example.morsecode;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class test extends AppCompatActivity {

    private Button submit,back,show;
    private EditText input;
    private TextView output,err1;
    private int length=0,min=0,max=0,key=0;
    private String out="";
    private String ans="";
    private String eng[]={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8","9","0"};
    private String morse[]={".-","-...","-.-.","-..",".","..-.","--.","....","..",".---","-.-",".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...-",".--","-..-","-.--","--..",".----","..---","...--","....-",".....","-....","--...","---..","----.","-----"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        min=getIntent().getIntExtra("min",0);
        max=getIntent().getIntExtra("max",0);
        key=getIntent().getIntExtra("key",0);

        submit=findViewById(R.id.submit);
        back=findViewById(R.id.back);
        show=findViewById(R.id.show);
        input=findViewById(R.id.input);
        output=findViewById(R.id.output);
        err1=findViewById(R.id.err1);

        input.setHint("Enter in "+(key==2?"Morsecode":"English"));

        generate();

        show.setOnClickListener(v -> {
            err1.setText("");
            input.setText(ans);
            submit.setText("New sequence");
        });

        back.setOnClickListener(v -> {
            err1.setText("");
            finish();
        });

        submit.setOnClickListener(v -> {
            if(submit.getText().toString().equals("New sequence")) {
                generate();
                submit.setText("Check");
                input.setText("");
                err1.setText("");
                return;
            }
            String in=input.getText().toString().trim();
            if(in.isEmpty()){
                err1.setTextColor(Color.parseColor("#FF0000"));
                err1.setText("*Please Enter Answer");
                return;
            }
            if(ans.equalsIgnoreCase(in)){
                err1.setTextColor(Color.parseColor("#00FF00"));
                err1.setText("Correct Answer");
                submit.setText("New sequence");
            } else {
                err1.setTextColor(Color.parseColor("#FF0000"));
                err1.setText("*Wrong Answer");
            }
        });
    }
    private void  generate(){
        out="";
        ans="";
        if(min==max){
            length=min;
        } else {
            length=(int)(Math.random()*(max-min+1)+min);
        }
        if(key==2){
            for(int i=0;i<length;i++){
                int x=(int)(Math.random()*36);
                out+=eng[x];
                ans+=morse[x]+" ";
            }
        } else if(key==1){
            for(int i=0;i<length;i++){
                int x=(int)(Math.random()*36);
                out+=morse[x]+" ";
                ans+=eng[x];
            }
        }
        ans=ans.trim();
        out=out.trim();
        output.setText(out);
    }
}