package com.example.manosyollas.actividades;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.manosyollas.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class RegistrateActivity extends AppCompatActivity {

    EditText username, password, docNumber, firstName, lastName, phone, email;
    Spinner docTypeSpinner, districtSpinner, ollaSpinner;
    Button btnCreate;

    private static final String URL_REGISTER = "http://manosyollas.atwebpages.com/services/Agregar_Usuario.php";
    private static final String URL_OLLAS = "http://manosyollas.atwebpages.com/services/Mostrar_Olla.php";
    private static final String URL_DISTRITOS = "http://manosyollas.atwebpages.com/services/Mostrar_Distrito.php";

    ArrayList<String> distritosList = new ArrayList<>();
    ArrayList<String> ollasList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrate);

        username = findViewById(R.id.Username);
        password = findViewById(R.id.regTxtContraseña);
        docNumber = findViewById(R.id.regTxtDni);
        firstName = findViewById(R.id.regTxtNombre);
        lastName = findViewById(R.id.regTxtApellido);
        phone = findViewById(R.id.regTxtTelefono);
        email = findViewById(R.id.regTxtCorreo);

        docTypeSpinner = findViewById(R.id.regCboTipoDocumento);
        districtSpinner = findViewById(R.id.regCboDistritos);
        ollaSpinner = findViewById(R.id.regCboOllaComun);

        btnCreate = findViewById(R.id.regBtnCrear);

        loadTipoDocumento();
        loadDistritos();
        loadOllas();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void loadTipoDocumento() {
        ArrayList<String> tipos = new ArrayList<>(Arrays.asList("DNI", "RUC", "Otros"));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        docTypeSpinner.setAdapter(adapter);
    }

    private void loadDistritos() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL_DISTRITOS, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        JSONArray data = response.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            distritosList.add(item.getString("nombre"));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistrateActivity.this, android.R.layout.simple_spinner_item, distritosList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        districtSpinner.setAdapter(adapter);
                    } else {
                        Toast.makeText(RegistrateActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(RegistrateActivity.this, "Error al procesar distritos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadOllas() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL_OLLAS, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        JSONArray data = response.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            ollasList.add(item.getString("nombre"));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistrateActivity.this, android.R.layout.simple_spinner_item, ollasList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ollaSpinner.setAdapter(adapter);
                    } else {
                        Toast.makeText(RegistrateActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(RegistrateActivity.this, "Error al procesar ollas.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        RequestParams params = new RequestParams();
        params.put("username", username.getText().toString());
        params.put("password", password.getText().toString());
        params.put("tipo_documento", docTypeSpinner.getSelectedItem().toString());
        params.put("numero_documento", docNumber.getText().toString());
        params.put("nombre", firstName.getText().toString());
        params.put("apellido", lastName.getText().toString());
        params.put("telefono", phone.getText().toString());
        params.put("email", email.getText().toString());
        params.put("fechaUltimaSesion", currentDate);
        params.put("sexo", "N/A");
        params.put("distritoId", districtSpinner.getSelectedItemPosition() + 1);
        params.put("ollaId", ollaSpinner.getSelectedItemPosition() + 1);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(URL_REGISTER, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    String message = response.getString("message");
                    Toast.makeText(RegistrateActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(RegistrateActivity.this, "Error al procesar la respuesta.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(RegistrateActivity.this, "Error de conexión con el servidor.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
