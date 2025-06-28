
package com.example.try16;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cam extends AppCompatActivity {

    private Bitmap capturedImage;
    private List<Pair<String,Bitmap>> referenceImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

        Button captureButton = findViewById(R.id.btnCapture);
        captureButton.setOnClickListener(this::dispatchTakePictureIntent);


        loadReferenceImages();
    }

    private void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureLauncher.launch(takePictureIntent);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    private final ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            capturedImage = (Bitmap) extras.get("data");


                            compareImages();
                        }
                    }
                }
            }
    );

private void loadReferenceImages() {
    referenceImages = new ArrayList<>();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference imagesRef = storageRef.child("images/");

    imagesRef.listAll().addOnSuccessListener(listResult -> {
        for (StorageReference imageRef : listResult.getItems()) {
            String fileName = imageRef.getName();
            imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                referenceImages.add(new Pair<>(fileName, bitmap));
            }).addOnFailureListener(exception -> {
                Log.e("TAG", "Error downloading image", exception);
                Toast.makeText(Cam.this, "Error downloading reference image", Toast.LENGTH_SHORT).show();
            });
        }
    }).addOnFailureListener(e -> {
        Log.e("TAG", "Error listing images", e);
        Toast.makeText(Cam.this, "Error listing reference images", Toast.LENGTH_SHORT).show();
    });
}

    private void compareImages() {
        if (capturedImage != null && !referenceImages.isEmpty()) {
            List<List<Landmark>> capturedLandmarks = detectFacialLandmarks(capturedImage);
            boolean matchFound = false;
            String matchingFileName = "";

            for (Pair<String, Bitmap> referenceImage : referenceImages) {
                Bitmap bitmap = referenceImage.second;
                List<List<Landmark>> referenceLandmarks = detectFacialLandmarks(bitmap);
                if (capturedLandmarks.size() == 1 && referenceLandmarks.size() == 1) {
                    if (areLandmarksSimilar(capturedLandmarks.get(0), referenceLandmarks.get(0), bitmap)) {
                        matchFound = true;
                        matchingFileName = referenceImage.first;
                        String name=matchingFileName.split("\\.")[0];
                        updatepresent(name);

                        break;
                    }
                }
            }

            if (matchFound) {
                Toast.makeText(Cam.this, "Facial recognition successful: Same person detected from ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Cam.this, Done.class));

            } else {
                Toast.makeText(Cam.this, "Facial recognition failed: No matching person detected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Cam.this, Update.class));

            }
        } else {
            Toast.makeText(Cam.this, "Reference images not loaded yet", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatepresent(String name) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Student");
        Query updatePresent = reference.orderByChild("rollNo").equalTo(name);
        updatePresent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String presentCountFromDb = dataSnapshot.child("presentCount").getValue(String.class);
                        int count = Integer.parseInt(presentCountFromDb != null ? presentCountFromDb : "0") + 1;
                        String presentCount = String.valueOf(count);
                        dataSnapshot.getRef().child("presentCount").setValue(presentCount);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private List<List<Landmark>> detectFacialLandmarks(Bitmap image) {
        FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        if (!detector.isOperational()) {
            Toast.makeText(this, "Face detector not operational", Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        }

        Frame frame = new Frame.Builder().setBitmap(image).build();
        SparseArray<Face> faces = detector.detect(frame);

        List<List<Landmark>> facialLandmarks = new ArrayList<>();
        for (int i = 0; i < faces.size(); i++) {
            Face face = faces.valueAt(i);
            List<Landmark> landmarks = face.getLandmarks();
            facialLandmarks.add(landmarks);
        }

        detector.release();
        return facialLandmarks;
    }


    private boolean areLandmarksSimilar(List<Landmark> capturedLandmarks, List<Landmark> referenceLandmarks, Bitmap referenceImage) {

        capturedLandmarks.size();
        referenceLandmarks.size();


        double xThreshold = 20;
        double yThreshold = 20;


        for (int i = 0; i < capturedLandmarks.size(); i++) {
            Landmark capturedLandmark = capturedLandmarks.get(i);
            Landmark referenceLandmark = referenceLandmarks.get(i);


            double xDiff = Math.abs(capturedLandmark.getPosition().x - referenceLandmark.getPosition().x);
            double yDiff = Math.abs(capturedLandmark.getPosition().y - referenceLandmark.getPosition().y);


            if (xDiff > xThreshold || yDiff > yThreshold) {
                return false;
            }
        }


        return true;
    }
}
//}