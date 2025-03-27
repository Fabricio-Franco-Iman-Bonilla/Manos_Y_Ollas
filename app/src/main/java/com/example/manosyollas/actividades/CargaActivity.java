package com.example.manosyollas.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.manosyollas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CargaActivity extends AppCompatActivity {
ProgressBar barCarga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carga);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        barCarga = findViewById(R.id.carBarCarga);
        Thread tCarga = new Thread(new Runnable() {
            @Override
            public void run() {
                //validar y pintar la barra de progreso
                for(int i=0; i <barCarga.getMax(); i++) {
                    barCarga.setProgress(i);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
                SharedPreferences sharedPrefs = getSharedPreferences("userPreferences", MODE_PRIVATE);
                boolean isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false);
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(isLoggedIn || currentUser != null) {

                    Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
                    menu.putExtra("nombre","Cachimbo UPN");
                    menu.putExtra("id", 2);
                    startActivity(menu);
                    finish();

                }else{
                    //llamar a la otra actividad
                    Intent sesion = new Intent(getApplicationContext(), InicioActivity.class);
                    startActivity(sesion);
                    finish(); //

                }

            }
        });//creamos hilo
        tCarga.start();
    }
}