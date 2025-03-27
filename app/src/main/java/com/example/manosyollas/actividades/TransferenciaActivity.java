package com.example.manosyollas.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.manosyollas.R;
import com.example.manosyollas.clases.Menu;
import com.example.manosyollas.fragmentos.DonacionesFragment;

public class TransferenciaActivity extends AppCompatActivity implements Menu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_transferencia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView icFlotante = findViewById(R.id.ic_flotante);
        icFlotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton btnYape = findViewById(R.id.btn_yape);
        btnYape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransferenciaActivity.this, YapeActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btnPlin = findViewById(R.id.btn_plin);
        btnPlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransferenciaActivity.this, PlinActivity.class);
                startActivity(intent);
            }
        });
        ImageButton btn_visa = findViewById(R.id.btn_visa);
        btn_visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransferenciaActivity.this, CuentasActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onClickMenu(int id) {
        Intent menu = new Intent(this, MenuActivity.class);
        menu.putExtra("id", id);
        startActivity(menu);
        finish();
    }
}