package com.example.try16;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {
    TextView t1;
    EditText f1, l1, tid1, p1, e1, c1;
    Button b1;
   FirebaseDatabase database;
   DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        t1 = findViewById(R.id.log1);
        f1 = findViewById(R.id.fir1);
        l1 = findViewById(R.id.las1);
        tid1 = findViewById(R.id.tea1);
        e1 = findViewById(R.id.ema1);
        p1 = findViewById(R.id.pas1);
        b1 = findViewById(R.id.btn1);
        c1 = findViewById(R.id.con1);
        // DB=new DBHelper(this);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(signup.this, login.class);
                startActivity(i);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database= FirebaseDatabase.getInstance();
                reference=database.getReference("users");

                String first, last, email, password, confirm, Tid;
                first = f1.getText().toString();
                last = l1.getText().toString();
                Tid = tid1.getText().toString();
                email = e1.getText().toString();
                password = p1.getText().toString();
                confirm = c1.getText().toString();
HelperClass helperClass=new HelperClass(first,last,email,password,confirm,Tid);
reference.child(first).setValue(helperClass);
Toast.makeText(signup.this,"You have signup successfully!",Toast.LENGTH_SHORT).show();
Intent intent=new Intent(signup.this,login.class);
startActivity(intent);

















            }
        });
    }
}