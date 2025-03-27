package com.example.manosyollas.actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.manosyollas.R;
import com.example.manosyollas.clases.Menu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SuministroActivity extends AppCompatActivity implements Menu {

    private ArrayList<String> itemsSeleccionados;
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "SeleccionPrefs";
    private static final String KEY_ITEMS = "itemsSeleccionados";
    private boolean goingToConfirmarActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suministro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        itemsSeleccionados = new ArrayList<>(preferences.getStringSet(KEY_ITEMS, new HashSet<>()));

        ImageView btnVolver = findViewById(R.id.ic_flotante);
        btnVolver.setOnClickListener(v -> finish());

        configurarProductos();
        restaurarSeleccion();

        Button btnAceptar = findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(v -> {
            if (itemsSeleccionados.isEmpty()) {
                Toast.makeText(SuministroActivity.this, "No se ha seleccionado ningún ítem", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences.Editor editor = preferences.edit();
            editor.putStringSet(KEY_ITEMS, new HashSet<>(itemsSeleccionados));
            editor.apply();

            goingToConfirmarActivity = true;
            Intent intent = new Intent(SuministroActivity.this, ConfirmarActivity.class);
            intent.putStringArrayListExtra("itemsSeleccionados", itemsSeleccionados);
            startActivity(intent);
            finish();
        });
    }

    private void restaurarSeleccion() {
        if (itemsSeleccionados.contains("Arroz - 5kg")) {
            ImageView icArroz = findViewById(R.id.ic_arroz);
            icArroz.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Aceite - 1L")) {
            ImageView icAceite = findViewById(R.id.ic_aceite);
            icAceite.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Harina - 3kg")) {
            ImageView icHarina = findViewById(R.id.ic_harina);
            icHarina.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Sal - 10kg")) {
            ImageView icSal = findViewById(R.id.ic_sal);
            icSal.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Azúcar - 2kg")) {
            ImageView icAzucar = findViewById(R.id.ic_azucar);
            icAzucar.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Leche - 1L")) {
            ImageView icLeche = findViewById(R.id.ic_leche);
            icLeche.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Fideos - 500g")) {
            ImageView icFideos = findViewById(R.id.ic_fideos);
            icFideos.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Lentejas - 1kg")) {
            ImageView icLentejas = findViewById(R.id.ic_lentejas);
            icLentejas.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Conservas - 400g")) {
            ImageView icConservas = findViewById(R.id.ic_conservas);
            icConservas.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Huevos - docena")) {
            ImageView icHuevos = findViewById(R.id.ic_huevos);
            icHuevos.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Atún - lata")) {
            ImageView icAtun = findViewById(R.id.ic_atun);
            icAtun.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Cereales - 500g")) {
            ImageView icCereales = findViewById(R.id.ic_cereales);
            icCereales.setAlpha(0.5f);
        }
    }

    private void configurarProductos() {
        ImageView icArroz = findViewById(R.id.ic_arroz);
        ImageView icAceite = findViewById(R.id.ic_aceite);
        ImageView icHarina = findViewById(R.id.ic_harina);
        ImageView icSal = findViewById(R.id.ic_sal);
        ImageView icAzucar = findViewById(R.id.ic_azucar);
        ImageView icLeche = findViewById(R.id.ic_leche);
        ImageView icFideos = findViewById(R.id.ic_fideos);
        ImageView icLentejas = findViewById(R.id.ic_lentejas);
        ImageView icConservas = findViewById(R.id.ic_conservas);
        ImageView icHuevos = findViewById(R.id.ic_huevos);
        ImageView icAtun = findViewById(R.id.ic_atun);
        ImageView icCereales = findViewById(R.id.ic_cereales);

        icArroz.setOnClickListener(v -> toggleSelection("Arroz - 5kg", icArroz));
        icAceite.setOnClickListener(v -> toggleSelection("Aceite - 1L", icAceite));
        icHarina.setOnClickListener(v -> toggleSelection("Harina - 3kg", icHarina));
        icSal.setOnClickListener(v -> toggleSelection("Sal - 10kg", icSal));
        icAzucar.setOnClickListener(v -> toggleSelection("Azúcar - 2kg", icAzucar));
        icLeche.setOnClickListener(v -> toggleSelection("Leche - 1L", icLeche));
        icFideos.setOnClickListener(v -> toggleSelection("Fideos - 500g", icFideos));
        icLentejas.setOnClickListener(v -> toggleSelection("Lentejas - 1kg", icLentejas));
        icConservas.setOnClickListener(v -> toggleSelection("Conservas - 400g", icConservas));
        icHuevos.setOnClickListener(v -> toggleSelection("Huevos - docena", icHuevos));
        icAtun.setOnClickListener(v -> toggleSelection("Atún - lata", icAtun));
        icCereales.setOnClickListener(v -> toggleSelection("Cereales - 500g", icCereales));
    }

    private void toggleSelection(String item, ImageView imageView) {
        if (itemsSeleccionados.contains(item)) {
            itemsSeleccionados.remove(item);
            imageView.setAlpha(1.0f);
            Toast.makeText(this, item + " deseleccionado", Toast.LENGTH_SHORT).show();
        } else {
            itemsSeleccionados.add(item);
            imageView.setAlpha(0.5f);
            Toast.makeText(this, item + " seleccionado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!goingToConfirmarActivity) {
            preferences.edit().clear().apply();
        }
    }

    @Override
    public void onClickMenu(int id) {
        Intent menu = new Intent(this, MenuActivity.class);
        menu.putExtra("id", id);
        startActivity(menu);

        finish();
    }
}
