package com.example.try16;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
public class login extends AppCompatActivity {
    TextView t1;
    EditText use, pas;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        t1 = findViewById(R.id.sig1);
        use = findViewById(R.id.use1);
        pas = findViewById(R.id.pas1);
        b1 = findViewById(R.id.btn1);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this, signup.class);
                startActivity(i);
            }
        });

        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (validateusername() && validatepassword()) {
                    checkuser();
                }
            }
        });
    }

    public Boolean validateusername() {
        String username = use.getText().toString();
        if (username.isEmpty()) {
            use.setError("Username cannot be empty");
            return false;
        } else {
            use.setError(null);
            return true;
        }
    }

    public Boolean validatepassword() {
        String password = pas.getText().toString();
        if (password.isEmpty()) {
            pas.setError("Password cannot be empty");
            return false;
        } else {
            pas.setError(null);
            return true;
        }
    }

    public void checkuser () {
        String username = use.getText().toString();
        String password = pas.getText().toString();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("first").equalTo(username);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String passwordfromDB = userSnapshot.child("password").getValue(String.class);
                        if (passwordfromDB != null && passwordfromDB.equals(password)) {
                            // Passwords match, login successful
                            Intent intent = new Intent(login.this, Home.class);
                            startActivity(intent);
                            return;
                        }
                    }
                    // Password does not match
                    pas.setError("Invalid Credentials");
                    pas.requestFocus();
                } else {
                    // User does not exist
                    use.setError("User does not exist");
                    use.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(login.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}