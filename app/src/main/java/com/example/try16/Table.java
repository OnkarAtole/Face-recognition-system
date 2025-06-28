package com.example.try16;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Table extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseDatabase database;
    private DatabaseReference studentsRef;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        studentsRef = database.getReference("Student");

        // Reference to the TableLayout
        tableLayout = findViewById(R.id.tableLayout);

        // Read data from Firebase
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the existing rows from the table
                tableLayout.removeAllViews();

                // Iterate through each child node (student)
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    // Get the data as a map
                    Map<String, Object> studentData = (HashMap<String, Object>) studentSnapshot.getValue();

                    // Add a row for each student
                    addTableRow(studentData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void addTableRow(Map<String, Object> studentData) {
        // Create a new row
        TableRow row = new TableRow(this);


        TextView rollNoTextView = new TextView(this);
        rollNoTextView.setText(studentData.get("rollNo").toString());
        row.addView(rollNoTextView);

        TextView nameTextView = new TextView(this);
        nameTextView.setText(studentData.get("name").toString());
        row.addView(nameTextView);

        TextView presentCountTextView = new TextView(this);
        presentCountTextView.setText(studentData.get("presentCount").toString());
        row.addView(presentCountTextView);

        // Add the row to the TableLayout
        tableLayout.addView(row);
    }
}