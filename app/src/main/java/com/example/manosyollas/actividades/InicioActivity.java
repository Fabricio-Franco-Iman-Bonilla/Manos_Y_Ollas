package com.example.manosyollas.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.manosyollas.R;

public class InicioActivity extends AppCompatActivity implements View.OnClickListener {
Button btnIngresar, btnRegistrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnIngresar = findViewById(R.id.btnIngresar);
        btnRegistrar = findViewById(R.id.btnRegistrate);

        btnIngresar.setOnClickListener(this);
        btnRegistrar.setOnClickListener(this);

    }
    //hola

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btnIngresar)
            ingresar();
        else if(v.getId() == R.id.btnRegistrate)
            registrate();
    }

    private void registrate() {
        Intent registro = new Intent(this, RegistrateActivity.class);
        startActivity(registro);
        finish();
    }

    private void ingresar() {
        Intent ingresar = new Intent(this, LoginActivity.class);
        startActivity(ingresar);
        finish();
    }
}