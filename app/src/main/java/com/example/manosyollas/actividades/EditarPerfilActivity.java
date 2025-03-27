package com.example.manosyollas.actividades;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.manosyollas.R;
import com.example.manosyollas.fragmentos.InicioFragment;
import com.example.manosyollas.fragmentos.MenuFragment;
import com.example.manosyollas.fragmentos.PerfilFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.security.MessageDigest;

import cz.msebera.android.httpclient.Header;

public class EditarPerfilActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner cboDistritos;
    EditText editTxtDni, editTxtNombre, editTxtApellido, editTxtCorreo, editTxtContrasena,editTxtContrasena2;
    TextView editTxtFechaNac;
    private SharedPreferences sharedPreferences;
    private static final String URL_MOSTRAR_USUARIO = "http://manosyollas.atwebpages.com/services/MostrarUsuarioxid.php"; // Cambia esto por tu enlace
    private final static String urlMostrarDistritos = "http://manosyollas.atwebpages.com/services/MostrarDistrito.php";
    private static final String URL_REGISTER = "http://manosyollas.atwebpages.com/services/EditarUsuario.php";
    Integer idUsuario;
    RadioGroup grpSexo;
    RadioButton rbtSinDefinir, rbtFemenino, rbtMasculino;
    Button btnCrear;
    Button  btnRegresar;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cboDistritos = findViewById(R.id.editPerCboDistritos);
        editTxtDni = findViewById(R.id.editPerTxtDni);
        editTxtNombre = findViewById(R.id.editPerTxtNombre);
        editTxtApellido = findViewById(R.id.editPerTxtApellido);
        editTxtCorreo = findViewById(R.id.editPerTxtCorreo);
        editTxtFechaNac = findViewById(R.id.editPerTxtFechanac);
        btnCrear = findViewById(R.id.editPerBtnCrear);
        editTxtContrasena = findViewById(R.id.editPerTxtContraseña);

        btnRegresar = findViewById(R.id.editPerBtnRegresar);

        grpSexo = findViewById(R.id.editPerRgpSexo);
        rbtSinDefinir = findViewById(R.id.editPerRbtSinDefinir);
        rbtMasculino = findViewById(R.id.editPerRbtMasculino);
        rbtFemenino = findViewById(R.id.editPerRbtFemenino);
        editTxtContrasena2=findViewById(R.id.editPerTxtContraseña2);

        sharedPreferences = getSharedPreferences("IdUsuario", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt("idUsuario", -1);

        if (idUsuario != -1) {
            mostrarUsuario(idUsuario);
        }
        editTxtFechaNac.setOnClickListener(this);
        btnCrear.setOnClickListener(this);

        btnRegresar.setOnClickListener(v -> finish());


        llenarDistritos();
    }

    private void llenarDistritos() {
        // Crear un objeto para realizar la tarea asíncrona hacia el web service
        AsyncHttpClient ahcMostrarDistritos = new AsyncHttpClient();

        // Configurar el Spinner con un mensaje por defecto mientras se cargan los distritos
        cboDistritos.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"-- Seleccione Distrito --"}));

        // Ejecutar la tarea asíncrona
        ahcMostrarDistritos.get(urlMostrarDistritos, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if (statusCode == 200) {
                    try {
                        // Convertir la respuesta JSON a un JSONArray
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);

                        // Crear un array para almacenar los distritos
                        String[] distritos = new String[jsonArray.length() + 1];
                        distritos[0] = "-- Seleccione Distrito --"; // Opción por defecto

                        // Llenar el array de distritos con los nombres obtenidos del JSON
                        for (int i = 1; i < jsonArray.length() + 1; i++) {
                            distritos[i] = jsonArray.getJSONObject(i - 1).getString("nombre_distrito");
                        }

                        // Actualizar el adaptador del Spinner con los distritos obtenidos
                        cboDistritos.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                distritos));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Toast.makeText(getApplicationContext(), "ERROR: " + statusCode, Toast.LENGTH_LONG).show();
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });

    }


    // Método para obtener el sexo seleccionado
    private String obtenerSexo() {
        // Obtener el ID del RadioButton seleccionado
        int selectedId = grpSexo.getCheckedRadioButtonId();

        // Verificar si hay un RadioButton seleccionado
        if (selectedId != -1) {
            // Obtener el RadioButton que fue seleccionado
            RadioButton selectedRadioButton = findViewById(selectedId);
            // Retornar el texto del RadioButton seleccionado
            return selectedRadioButton.getText().toString();
        } else {
            // Retornar un valor por defecto si no se seleccionó nada
            return "Sin definir";
        }
    }


    private void mostrarUsuario(int idUsuario) {
        String url = URL_MOSTRAR_USUARIO + "?idUsuario=" + idUsuario;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    editTxtDni.setText(response.getString("dni"));
                    editTxtNombre.setText(response.getString("nombre"));
                    editTxtApellido.setText(response.getString("apellidos"));
                    editTxtCorreo.setText(response.getString("correo"));
                    editTxtFechaNac.setText(response.getString("fecha_nac"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), "Error al obtener los datos", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.editPerTxtFechanac)
            seleccionarFecha();
        else if (view.getId() == R.id.editPerBtnCrear)
            editarCuenta();
        //else if (view.getId() == R.id.editPerBtnRegresar)
         //   regresar();


    }
    //private void regresar() {
     //   Intent sesion = new Intent(EditarPerfilActivity.this, PerfilFragment.class );
     //   startActivity(sesion);
      //  finish();}


    private void seleccionarFecha() {

        DatePickerDialog dpd;
        final Calendar fechaActual = Calendar.getInstance();
        int dia = fechaActual.get(Calendar.DAY_OF_MONTH);
        int mes = fechaActual.get(Calendar.MONTH);
        int anho = fechaActual.get(Calendar.YEAR);
        dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                editTxtFechaNac.setText(y+"-"+((m+1)<10 ?"0"+(m+1):(m+1))+"-"+(d<10?"0"+d:d));
            }
        }, anho, mes, dia);
        dpd.show();

    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void editarCuenta() {

        if (validarFormulario()) {
            String dni = editTxtDni.getText().toString().trim();
            String nombre = editTxtNombre.getText().toString().trim();
            String apellido = editTxtApellido.getText().toString().trim();
            String fechaNac = editTxtFechaNac.getText().toString().trim();
            String sexo = obtenerSexo();
            String correo = editTxtCorreo.getText().toString().trim();

            String contrasena = editTxtContrasena.getText().toString().trim();
            String clavehash = hashPassword(contrasena);
            Integer distrito = cboDistritos.getSelectedItemPosition();
            String distritocv = String.valueOf(distrito);

            String usuarioid = String.valueOf(idUsuario);
            editaruser(usuarioid, dni, nombre, apellido, fechaNac, sexo, correo, clavehash, distritocv);
        }
    }

    private boolean validarFormulario() {
        String dni = editTxtDni.getText().toString().trim();
        String nombre = editTxtNombre.getText().toString().trim();
        String apellido = editTxtApellido.getText().toString().trim();
        String fechaNac = editTxtFechaNac.getText().toString().trim();
        String correo = editTxtCorreo.getText().toString().trim();
        String contrasena = editTxtContrasena.getText().toString().trim();
        String contrasena2 = editTxtContrasena2.getText().toString().trim();

        if (dni.isEmpty()) {
            Toast.makeText(this, "DNI no puede estar vacio",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (nombre.isEmpty()) {
            Toast.makeText(this, "Nombre no puede estar vacío",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (apellido.isEmpty()) {
            Toast.makeText(this, "Apellido no puede estar vacío",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fechaNac.isEmpty()) {
            Toast.makeText(this, "Fecha de nacimiento no puede estar vacía",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (correo.isEmpty()) {
            Toast.makeText(this, "Correo no puede estar vacío",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (contrasena.isEmpty()) {
            Toast.makeText(this, "Contraseña no puede estar vacía",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (contrasena2.isEmpty()) {
            Toast.makeText(this, "Confirmación de contraseña no puede estar vacía",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!contrasena.equals(contrasena2)) {
            Toast.makeText(this, "Las contraseñas no coinciden",
                    Toast.LENGTH_SHORT).show();
            return false;
        }


        int selectedSexId = grpSexo.getCheckedRadioButtonId();
        if (selectedSexId == -1) {
            Toast.makeText(this, "Debe seleccionar un sexo",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void editaruser(String iduser, String dni, String nombre, String apellido, String fechaNac, String sexo, String correo, String contrasena, String distritocv) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("idUsuario", idUsuario);  // Agregar el idUsuario que se va a editar
        params.put("dni", dni);
        params.put("nombre", nombre);
        params.put("apellidos", apellido);
        params.put("fecha_nac", fechaNac);
        params.put("sexo", sexo);
        params.put("correo", correo);
        params.put("clave", contrasena);  // Hashea la clave si es necesario
        params.put("id_distrito", distritocv);

        client.post(URL_REGISTER, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    // Verificar el mensaje de éxito en la respuesta del servidor
                    String message = response.getString("message");
                    if (message.equals("Usuario editado exitosamente")) {
                        Toast.makeText(EditarPerfilActivity.this, "Usuario editado correctamente", Toast.LENGTH_SHORT).show();
                        // Opcional: puedes redirigir a otra actividad después de la edición
                        //SI LO DESCOMENTAS SE CAE
                        //Intent intent = new Intent(EditarPerfilActivity.this, MenuActivity.class);
                        //startActivity(intent);
                        //finish();

                        //MI INTENTO DE LEVANTAR LA MONDÁ- ARREGLADO CARAJO
                        Intent menu = new Intent(EditarPerfilActivity.this, MenuActivity.class);
                        menu.putExtra("id", 0);
                        startActivity(menu);
                        finish();
                    } else {
                        Toast.makeText(EditarPerfilActivity.this, "Error al editar usuario", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EditarPerfilActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(EditarPerfilActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

}