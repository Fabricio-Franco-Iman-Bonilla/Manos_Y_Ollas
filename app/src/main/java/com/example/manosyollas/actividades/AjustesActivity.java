package com.example.manosyollas.actividades;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.manosyollas.R;

public class AjustesActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private SharedPreferences sharedPreferences;
    private CheckBox ajusChkTemaOscuro;
    Spinner cboIdiomas;
    CheckBox chkNotificaciones, chkTemaOscuro;
    TextView lblSonido;
    SeekBar barSonido;
    Button btnAplicar, btnRestaurar;
    ImageView icVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ajustes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ajusChkTemaOscuro = findViewById(R.id.ajusChkTemaOscuro);
        sharedPreferences = getSharedPreferences("ajustes", MODE_PRIVATE);

        cboIdiomas = findViewById(R.id.ajusCboIdiomas);
        chkNotificaciones = findViewById(R.id.ajusChkNotificaciones);
        lblSonido = findViewById(R.id.ajusLblSonido);
        barSonido = findViewById(R.id.ajusBarSonido);
        btnAplicar = findViewById(R.id.ajusBtnAplicar);
        btnRestaurar = findViewById(R.id.ajusBtnRestaurar);
        chkTemaOscuro = findViewById(R.id.ajusChkTemaOscuro);

        btnAplicar.setOnClickListener(this);
        btnRestaurar.setOnClickListener(this);
        barSonido.setOnSeekBarChangeListener(this);

        llenarIdiomas();
        cargarPreferencias();
    }

    private void cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("configuracion", Context.MODE_PRIVATE);
        int idioma = preferences.getInt("idioma", 0);
        boolean notificaciones = preferences.getBoolean("notificaciones", false);
        int sonido = preferences.getInt("sonido", 100);
        boolean temaOscuro = preferences.getBoolean("temaOscuro", false);

        cboIdiomas.setSelection(idioma);
        chkNotificaciones.setChecked(notificaciones);
        barSonido.setProgress(sonido);
        chkTemaOscuro.setChecked(temaOscuro);

        if (temaOscuro) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    private void llenarIdiomas() {
        String[] idiomas = {"Español", "Ingles", "Quechua", "Aymara", "Shipibo", "Aleman"};
        cboIdiomas.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, idiomas));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ajusBtnAplicar) {
            aplicar();
        } else if (view.getId() == R.id.ajusBtnRestaurar) {
            restaurar();
        }
    }

    private void aplicar() {
        SharedPreferences preferences = getSharedPreferences("configuracion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("idioma", cboIdiomas.getSelectedItemPosition());
        editor.putBoolean("notificaciones", chkNotificaciones.isChecked());
        editor.putInt("sonido", barSonido.getProgress());
        editor.putBoolean("temaOscuro", chkTemaOscuro.isChecked());
        editor.apply();

        if (chkTemaOscuro.isChecked()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        Toast.makeText(this, "Preferencias Guardadas", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void restaurar() {
        cboIdiomas.setSelection(0);
        chkNotificaciones.setChecked(false);
        barSonido.setProgress(100);
        chkTemaOscuro.setChecked(false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar == barSonido) {
            String mensaje = "Sonido de Notificaciones: ";
            switch (i) {
                case 0:
                    mensaje += "Silencio";
                    break;
                case 100:
                    mensaje += "Máximo";
                    break;
                default:
                    mensaje += String.valueOf(i);
                    break;
            }
            lblSonido.setText(mensaje);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPreferencias();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    public void aplicarTema() {
        SharedPreferences preferences = getSharedPreferences("configuracion", Context.MODE_PRIVATE);
        boolean temaOscuro = preferences.getBoolean("temaOscuro", false);

        if (temaOscuro) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}