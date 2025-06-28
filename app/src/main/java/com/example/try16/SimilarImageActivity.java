package com.example.try16;


import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class SimilarImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_image);

        // Retrieve the list of reference image URLs from the intent
        ArrayList<String> referenceImageUrls = getIntent().getStringArrayListExtra("reference_image_urls");

        // Load and display each reference image using Picasso library
        for (String imageUrl : referenceImageUrls) {
            ImageView imageView = new ImageView(this);
            Picasso.get().load(imageUrl).into(imageView);
            // Assuming you have a LinearLayout in your layout file where you want to display the images
            // Add imageView to your layout
        }
    }
}




