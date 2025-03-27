package com.example.manosyollas.actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import cz.msebera.android.httpclient.Header;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.example.manosyollas.R;
import com.loopj.android.http.AsyncHttpClient;

import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConfirmarActivity extends AppCompatActivity {
    Spinner cboollacomunes;
    Integer idUsuario;
    Button btnConfirmar;
    private SharedPreferences sharedPreferences;
    private final static String urlMostrarollas = "http://manosyollas.atwebpages.com/services/MostrarSoloOllas.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirmar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnConfirmar=findViewById(R.id.btnConfirmar);
        cboollacomunes = findViewById(R.id.sumCboOllasComunes);
        sharedPreferences = getSharedPreferences("IdUsuario", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt("idUsuario", -1);

        ArrayList<String> itemsSeleccionados = getIntent().getStringArrayListExtra("itemsSeleccionados");
        llenarollas();
        if (itemsSeleccionados != null && itemsSeleccionados.size() > 0) {
            Log.d("ConfirmarActivity", "Items recibidos: " + itemsSeleccionados.toString());
            Toast.makeText(this, "Datos recibidos", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("ConfirmarActivity", "No se recibieron datos.");
            Toast.makeText(this, "No se recibieron datos", Toast.LENGTH_SHORT).show();
        }

        LinearLayout layout = findViewById(R.id.linearLayoutItems);

        for (String item : itemsSeleccionados) {
            String[] parts = item.split(" - ");

            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            rowLayout.setPadding(16, 16, 16, 16);
            rowLayout.setGravity(Gravity.CENTER_HORIZONTAL);

            TextView productoTextView = new TextView(this);
            productoTextView.setText(parts[0]);
            productoTextView.setTextSize(16);
            productoTextView.setGravity(Gravity.CENTER);
            productoTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            TextView cantidadTextView = new TextView(this);
            cantidadTextView.setText(parts[1]);
            cantidadTextView.setTextSize(16);
            cantidadTextView.setGravity(Gravity.CENTER);
            cantidadTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            rowLayout.addView(productoTextView);
            rowLayout.addView(cantidadTextView);

            layout.addView(rowLayout);
        }

        ImageView icFlotante = findViewById(R.id.ic_flotante);
        icFlotante.setOnClickListener(v -> {
            // Inicio de sesión exitoso, redirigir a la siguiente Activity
            Intent principal = new Intent(ConfirmarActivity.this, MenuActivity.class);
            principal.putExtra("id", 5);
            startActivity(principal);
            finish();
        });

        Button btnEditar = findViewById(R.id.btnEditar);

        btnConfirmar.setOnClickListener(v -> {

            String usuarioId = idUsuario.toString(); // Obtén el ID del usuario real de SharedPreferences
            Integer ollaId=cboollacomunes.getSelectedItemPosition();

            String fechaDonacion = obtenerFechaActual();
            String donacionTipo = "Alimento";
            String donacionEstado = "Pendiente";
            registrarDonacion(usuarioId, ollaId, fechaDonacion, donacionTipo, donacionEstado, itemsSeleccionados);
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicio de sesión exitoso, redirigir a la siguiente Activity
                Intent principal = new Intent(ConfirmarActivity.this, MenuActivity.class);
                principal.putExtra("id", 5);
                startActivity(principal);
                finish();
            }
        });

    }

    private String obtenerFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }
    private void registrarDonacion(String usuarioId, Integer ollaId, String fechaDonacion, String donacionTipo, String donacionEstado, ArrayList<String> itemsSeleccionados) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("usuario_id", usuarioId);
        params.put("olla_id", ollaId);
        params.put("fecha_donacion", fechaDonacion);
        params.put("donacion_tipo", donacionTipo);
        params.put("donacion_estado", donacionEstado);

        client.post("http://manosyollas.atwebpages.com/services/insertardonacion.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        obtenerUltimoIdDonacion(usuarioId, itemsSeleccionados);
                    } else {
                        Toast.makeText(ConfirmarActivity.this, "Error al registrar donación", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ConfirmarActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(ConfirmarActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerUltimoIdDonacion(String usuarioId, ArrayList<String> itemsSeleccionados) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://manosyollas.atwebpages.com/services/obteneridonacion.php?usuario_id=" + usuarioId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.has("ultimoID")) {
                        String ultimoIdDonacion = response.getString("ultimoID");
                        for (String item : itemsSeleccionados) {
                            String[] parts = item.split(" - ");
                            registrarDetalleDonacion(ultimoIdDonacion, parts[0], parts[1]);
                        }
                        Toast.makeText(ConfirmarActivity.this, "Donación confirmada", Toast.LENGTH_SHORT).show();
                        Intent principal = new Intent(ConfirmarActivity.this, MenuActivity.class);
                        principal.putExtra("id", 4);
                        startActivity(principal);
                        finish();
                    } else {
                        Toast.makeText(ConfirmarActivity.this, "Error al obtener último ID de donación", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ConfirmarActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(ConfirmarActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registrarDetalleDonacion(String donacionId, String productoNombre, String productoCantidad) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("donacion_id", donacionId);
        params.put("producto_nombre", productoNombre);
        params.put("producto_cantidad", productoCantidad);

        client.post("http://manosyollas.atwebpages.com/services/insertardedonacion.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (!response.getBoolean("success")) {
                        Toast.makeText(ConfirmarActivity.this, "Error al registrar detalle de donación", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ConfirmarActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(ConfirmarActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void llenarollas() {

        AsyncHttpClient ahcMostrarDistritos = new AsyncHttpClient();


        cboollacomunes.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"-- Seleccione Distrito --"}));


        ahcMostrarDistritos.get(urlMostrarollas, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if (statusCode == 200) {
                    try {

                        JSONArray jsonArray = new JSONArray(rawJsonResponse);


                        String[] distritos = new String[jsonArray.length() + 1];
                        distritos[0] = "-- Seleccione Olla Comun --"; // Opción por defecto


                        for (int i = 1; i < jsonArray.length() + 1; i++) {
                            distritos[i] = jsonArray.getJSONObject(i - 1).getString("olla_nombre");
                        }


                        cboollacomunes.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                distritos));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Toast.makeText(getApplicationContext(), "ERROR: "+statusCode, Toast.LENGTH_LONG).show();
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });

    }


}