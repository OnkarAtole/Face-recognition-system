package com.example.try16;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Update extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private EditText editTextRollNo, editTextName;
    private ImageView imageViewSelected;
    private Bitmap selectedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Student");

        // Initialize Firebase Storage
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // Initialize EditText fields and ImageView
        editTextRollNo = findViewById(R.id.editTextRollNo);
        editTextName = findViewById(R.id.editTextName);
        imageViewSelected = findViewById(R.id.imageViewSelected);
    }

    public void onSelectImageClicked(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No camera app available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {

            Bundle extras = data.getExtras();
            selectedImageBitmap = (Bitmap) extras.get("data");

            imageViewSelected.setImageBitmap(selectedImageBitmap);
            imageViewSelected.setVisibility(View.VISIBLE);
        }
    }

    public void onAddPersonClicked(View view) {
        String rollNo = editTextRollNo.getText().toString().trim();
        String name = editTextName.getText().toString().trim();

        if (!rollNo.isEmpty() && !name.isEmpty() && selectedImageBitmap != null) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();


            uploadImage(rollNo, name, imageData);
        } else {
            Toast.makeText(this, "Please fill all fields and capture an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(final String rollNo, final String name, byte[] imageData) {
        StorageReference imageRef = mStorageRef.child("images/" + rollNo + ".jpg");
        UploadTask uploadTask = imageRef.putBytes(imageData);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Image upload successful, get download URL
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl = uri.toString();
                        // Add person data to Firebase Database
                        Helper helper = new Helper(rollNo, name, imageUrl,"0");
                        mDatabase.child(rollNo).setValue(helper)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Update.this, "Student added to Firebase", Toast.LENGTH_SHORT).show();
                                        // Clear EditText fields after adding person
                                        editTextRollNo.setText("");
                                        editTextName.setText("");
                                        imageViewSelected.setImageResource(0);
                                        imageViewSelected.setVisibility(View.GONE);
                                        selectedImageBitmap = null;
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Update.this, "Failed to add person to Firebase", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Update.this, "Failed to upload image to Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
