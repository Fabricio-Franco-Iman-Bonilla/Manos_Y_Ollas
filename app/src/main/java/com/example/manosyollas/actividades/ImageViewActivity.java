package com.example.manosyollas.actividades;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.manosyollas.R;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        ImageView imageView = findViewById(R.id.imageView);

        // Recibir el identificador de recurso de la imagen
        int resourceId = getIntent().getIntExtra("image", -1);
        if (resourceId != -1) {
            imageView.setImageResource(resourceId); // Muestra la imagen local en ImageView
        }
    }
}
